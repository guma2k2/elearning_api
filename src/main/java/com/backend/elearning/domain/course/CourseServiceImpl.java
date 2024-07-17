package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuiz;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuizRepository;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewRepository;
import com.backend.elearning.domain.section.SectionService;
import com.backend.elearning.domain.section.SectionVM;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.ConvertTitleToSlug;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final SectionService sectionService;

    private final LectureRepository lectureRepository ;
    private final ReviewRepository reviewRepository;
    private final LearningLectureRepository learningLectureRepository;
    private final LearningQuizRepository learningQuizRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository, UserRepository userRepository, SectionService sectionService, LectureRepository lectureRepository, ReviewRepository reviewRepository, LearningLectureRepository learningLectureRepository, LearningQuizRepository learningQuizRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.sectionService = sectionService;
        this.lectureRepository = lectureRepository;
        this.reviewRepository = reviewRepository;
        this.learningLectureRepository = learningLectureRepository;
        this.learningQuizRepository = learningQuizRepository;
    }


    @Override
    public PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize) {
        List<CourseVM> courseVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Course> coursePage = courseRepository.findAllCustom(pageable);
        List<Course> courses = coursePage.getContent();
        for (Course course : courses) {
            courseVMS.add(CourseVM.fromModel(course ,new ArrayList<>(),0, 0.0,0,"" ));

        }

        return new PageableData(
                pageNum,
                pageSize,
                (int) coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                courseVMS
        );
    }

    @Override
    public CourseVM create(CoursePostVM coursePostVM) {
        /*String email = SecurityContextHolder.getContext().getAuthentication().getName();*/
        String email = "thuanngo3072006@gmail.com";
        if (courseRepository.countExistByTitle(coursePostVM.title(), null) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.COURSE_TITLE_DUPLICATED);
        }
        Category category = categoryRepository.findById(coursePostVM.categoryId()).orElseThrow();
        Topic topic = topicRepository.findById(coursePostVM.topicId()).orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow();

        String slug = ConvertTitleToSlug.convertTitleToSlug(coursePostVM.title());
        Course course = Course.builder()
                .title(coursePostVM.title())
                .category(category)

                .topic(topic)
                .user(user)
                .slug(slug)
                .build();
        return CourseVM.fromModel(courseRepository.save(course), new ArrayList<>(),0, 0.0,0,"" );
    }

    @Override
    public CourseVM update(CoursePostVM coursePutVM, Long userId, Long courseId) {
        Course oldCourse = courseRepository.findByIdReturnSections(courseId).orElseThrow();
        if (oldCourse.getUser().getId() != userId) {
            throw new BadRequestException("You don't have permission to edit this course");
        }
        if (courseRepository.countExistByTitle(coursePutVM.title(), courseId) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.COURSE_TITLE_DUPLICATED);
        }
        if (!Objects.equals(oldCourse.getCategory().getId(), coursePutVM.categoryId())) {
            Category category = categoryRepository.findById(coursePutVM.categoryId()).orElseThrow();
            oldCourse.setCategory(category);
        }

        if (oldCourse.getTopic().getId() != coursePutVM.topicId()) {
            Topic topic = topicRepository.findById(coursePutVM.topicId()).orElseThrow();
            oldCourse.setTopic(topic);
        }
        oldCourse.setTitle(coursePutVM.title());
        oldCourse.setHeadline(coursePutVM.headline());
        oldCourse.setObjectives(coursePutVM.objectives());
        oldCourse.setRequirements(coursePutVM.requirements());
        oldCourse.setDescription(coursePutVM.description());
        oldCourse.setTargetAudiences(coursePutVM.targetAudiences());
        oldCourse.setFree(coursePutVM.free());
        if (coursePutVM.image() != "") {
            oldCourse.setImageId(coursePutVM.image());
        }
        oldCourse.setLevel(ELevel.valueOf(coursePutVM.level()));
        return CourseVM.fromModel(courseRepository.save(oldCourse), new ArrayList<>(),0, 0.0,0,"" );
    }

    @Override
    public CourseVM getCourseById(Long id) {
        Course course = courseRepository.findByIdReturnSections(id).orElseThrow();
        Long courseId = course.getId();
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        int ratingCount = reviews.size();
        Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);

        double roundedAverageRating = Math.round(averageRating * 10) / 10.0;

        AtomicInteger totalLectureCourse = new AtomicInteger();
        AtomicInteger totalDurationCourse = new AtomicInteger();
        course.getSections().forEach(section -> {
            Long sectionId = section.getId();
            List<Lecture> lectures = lectureRepository.findBySectionId(sectionId);
            int totalLectures = lectures.size();
            totalLectureCourse.addAndGet(totalLectures);
            int totalSeconds = lectures.stream()
                    .mapToInt(lecture -> lecture.getDuration())
                    .sum();
            totalDurationCourse.addAndGet(totalSeconds);

        });
        double roundedHours = Math.round(totalDurationCourse.get() * 2) / 7200.0;
        String formattedHours = String.format("%.1f hours", roundedHours);
        List<SectionVM> sections = new ArrayList<>(course.getSections()
                .stream().map(section -> sectionService.getById(section.getId())).toList());
        sections.sort(Comparator.comparing(SectionVM::number));
        return CourseVM.fromModel(course, sections,ratingCount, roundedAverageRating, totalLectureCourse.get(),formattedHours);
    }

    @Override
    public CourseLearningVm getCourseBySlug(String slug) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String email = "thuanngo3072002@gmail.com";
        Course course = courseRepository.findBySlugReturnSections(slug).orElseThrow();
        List<SectionVM> sections = new ArrayList<>(course.getSections()
                .stream().map(section -> sectionService.getById(section.getId())).toList());
        sections.sort(Comparator.comparing(SectionVM::number));

        CourseVM courseVM = CourseVM.fromModel(course, sections ,0, 0.0,0,"" );

        Optional<LearningLecture> maxAccessTimeByEmailAndCourseSlugLecture = learningLectureRepository.findMaxAccessTimeByEmailAndCourseSlug(email, slug);
        Optional<LearningQuiz> maxAccessTimeByEmailAndCourseSlugQuiz = learningQuizRepository.findMaxAccessTimeByEmailAndCourseSlug(email, slug);

        if (maxAccessTimeByEmailAndCourseSlugLecture.isPresent() && maxAccessTimeByEmailAndCourseSlugQuiz.isPresent()) {
            var lecture = maxAccessTimeByEmailAndCourseSlugLecture.get();
            var quiz = maxAccessTimeByEmailAndCourseSlugQuiz.get();
            if (lecture.getAccessTime().isAfter(quiz.getAccessTime())) {
                return new CourseLearningVm(courseVM, lecture.getLecture().getSection().getId(), lecture.getId(), lecture.getWatchingSecond());
            }
        } else if (maxAccessTimeByEmailAndCourseSlugLecture.isPresent()) {
            var lecture = maxAccessTimeByEmailAndCourseSlugLecture.get();
            return new CourseLearningVm(courseVM, lecture.getLecture().getSection().getId(), lecture.getLecture().getId(), lecture.getWatchingSecond());
        } else if (maxAccessTimeByEmailAndCourseSlugQuiz.isPresent()) {
            var quiz = maxAccessTimeByEmailAndCourseSlugQuiz.get();
            return new CourseLearningVm(courseVM, quiz.getQuiz().getSection().getId(), quiz.getQuiz().getId(), null);
        }
        return new CourseLearningVm(courseVM, null ,null, null);
    }

    @Override
    public PageableData<CourseVM> getCoursesByMultiQuery(int pageNum, int pageSize, String name, int rating) {

        return null;
    }
}
