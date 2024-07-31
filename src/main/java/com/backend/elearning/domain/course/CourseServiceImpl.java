package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuiz;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuizRepository;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizRepository;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewService;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionService;
import com.backend.elearning.domain.section.SectionVM;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserProfileVM;
import com.backend.elearning.domain.user.UserService;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.ConvertTitleToSlug;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final SectionService sectionService;
    private final QuizRepository quizRepository;
    private final LectureRepository lectureRepository ;
    private final ReviewService reviewService;
    private final LearningLectureRepository learningLectureRepository;
    private final LearningQuizRepository learningQuizRepository;

    private final LearningCourseRepository learningCourseRepository;
    private final UserService userService;

    private final static String LECTURE_TYPE = "lecture";
    private final static String QUIZ_TYPE = "quiz";

    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository, SectionService sectionService, QuizRepository quizRepository, LectureRepository lectureRepository, ReviewService reviewService, LearningLectureRepository learningLectureRepository, LearningQuizRepository learningQuizRepository, LearningCourseRepository learningCourseRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
        this.sectionService = sectionService;
        this.quizRepository = quizRepository;
        this.lectureRepository = lectureRepository;
        this.reviewService = reviewService;
        this.learningLectureRepository = learningLectureRepository;
        this.learningQuizRepository = learningQuizRepository;
        this.learningCourseRepository = learningCourseRepository;
        this.userService = userService;
    }


    @Override
    public PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize) {
        List<CourseVM> courseVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Course> coursePage = courseRepository.findAllCustom(pageable);
        List<Course> courses = coursePage.getContent();
        for (Course course : courses) {
            courseVMS.add(CourseVM.fromModel(course ,new ArrayList<>(),0, 0.0,0,"" , null, false));

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
        User user = userService.getByEmail(email);

        String slug = ConvertTitleToSlug.convertTitleToSlug(coursePostVM.title());
        Course course = Course.builder()
                .title(coursePostVM.title())
                .category(category)

                .topic(topic)
                .user(user)
                .slug(slug)
                .build();
        return CourseVM.fromModel(courseRepository.save(course), new ArrayList<>(),0, 0.0,0,"", null, false);
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
        return CourseVM.fromModel(courseRepository.save(oldCourse), new ArrayList<>(),0, 0.0,0,"", null , false);
    }

    @Override
    public CourseVM getCourseById(Long id) {
        Course course = courseRepository.findByIdReturnSections(id).orElseThrow();
        Long courseId = course.getId();
        List<Review> reviews = reviewService.findByCourseId(courseId);
        int ratingCount = reviews.size();
        Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);

        double roundedAverageRating = Math.round(averageRating * 10) / 10.0;

        AtomicInteger totalCurriculumCourse = new AtomicInteger();
        AtomicInteger totalDurationCourse = new AtomicInteger();
        course.getSections().forEach(section -> {
            Long sectionId = section.getId();
            List<Lecture> lectures = lectureRepository.findBySectionId(sectionId);
            int totalSeconds = lectures.stream()
                    .mapToInt(lecture -> lecture.getDuration())
                    .sum();
            totalDurationCourse.addAndGet(totalSeconds);

        });
        double roundedHours = Math.round(totalDurationCourse.get() * 2) / 7200.0;
        String formattedHours = String.format("%.1f hours", roundedHours);


        List<SectionVM> sections = new ArrayList<>(course.getSections()
                .stream().map(section -> sectionService.getById(section.getId(), null)).toList());


        sections.forEach(sectionVM -> {
            int countCurriculumPerSection = sectionVM.curriculums().size();
            totalCurriculumCourse.addAndGet(countCurriculumPerSection);
        });
        sections.sort(Comparator.comparing(SectionVM::number));

        Long userId = course.getUser().getId();
        UserProfileVM userProfileVM = userService.getById(userId);

        boolean learning = true;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null) {
            if (!learningCourseRepository.findByStudentAndCourse(email, courseId).isPresent()) {
                learning = false;
            }
        }
        return CourseVM.fromModel(course, sections,ratingCount, roundedAverageRating, totalCurriculumCourse.get(),formattedHours, userProfileVM, learning);
    }

    @Override
    public CourseListGetVM getCourseListGetVMById(Long id) {
        Course course = courseRepository.findByIdReturnSections(id).orElseThrow();
        Long courseId = course.getId();
        // get rating count, average rating
        List<Review> reviews = reviewService.findByCourseId(courseId);
        int ratingCount = reviews.size();
        Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);
        double roundedAverageRating = Math.round(averageRating * 10) / 10.0;

        // get totalCur, total duration of course by section
        AtomicInteger totalCurriculumCourse = new AtomicInteger();
        AtomicInteger totalDurationCourse = new AtomicInteger();


        course.getSections().forEach(section -> {
            Long sectionId = section.getId();
            List<Lecture> lectures = lectureRepository.findBySectionId(sectionId);
            List<Quiz> quizzes = quizRepository.findBySectionId(sectionId   );
            totalCurriculumCourse.addAndGet(lectures.size());
            totalCurriculumCourse.addAndGet(quizzes.size());
            int totalSeconds = lectures.stream()
                    .mapToInt(lecture -> lecture.getDuration())
                    .sum();
            totalDurationCourse.addAndGet(totalSeconds);
        });
        double roundedHours = Math.round(totalDurationCourse.get() * 2) / 7200.0;
        String formattedHours = String.format("%.1f hours", roundedHours);

        return CourseListGetVM.fromModel(course, formattedHours, totalCurriculumCourse.get(), roundedAverageRating, ratingCount);
    }

    @Override
    public CourseLearningVm getCourseBySlug(String slug) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        String email = "thuanngo3072002@gmail.com";
        Course course = courseRepository.findBySlugReturnSections(slug).orElseThrow();

        AtomicInteger totalCurriculumCourse = new AtomicInteger();

        List<SectionVM> sections = new ArrayList<>(course.getSections()
                .stream().map(section -> sectionService.getById(section.getId(), email)).toList());
        sections.forEach(sectionVM -> {
            int countCurriculumPerSection = sectionVM.curriculums().size();
            totalCurriculumCourse.addAndGet(countCurriculumPerSection);
        });
        sections.sort(Comparator.comparing(SectionVM::number));

        CourseVM courseVM = CourseVM.fromModel(course, sections ,0, 0.0, totalCurriculumCourse.get(),"", null, true );

        Optional<LearningLecture> maxAccessTimeByEmailAndCourseSlugLecture = learningLectureRepository.findMaxAccessTimeByEmailAndCourseSlug(email, slug);
        Optional<LearningQuiz> maxAccessTimeByEmailAndCourseSlugQuiz = learningQuizRepository.findMaxAccessTimeByEmailAndCourseSlug(email, slug);

        if (maxAccessTimeByEmailAndCourseSlugLecture.isPresent() && maxAccessTimeByEmailAndCourseSlugQuiz.isPresent()) {
            var lecture = maxAccessTimeByEmailAndCourseSlugLecture.get();
            var quiz = maxAccessTimeByEmailAndCourseSlugQuiz.get();
            if (lecture.getAccessTime().isAfter(quiz.getAccessTime())) {
                return new CourseLearningVm(courseVM, lecture.getLecture().getSection().getId(), lecture.getLecture().getId(), lecture.getWatchingSecond(), LECTURE_TYPE);
            } else {
                return new CourseLearningVm(courseVM, quiz.getQuiz().getSection().getId(), quiz.getQuiz().getId(), null, QUIZ_TYPE);
            }
        } else if (maxAccessTimeByEmailAndCourseSlugLecture.isPresent()) {
            var lecture = maxAccessTimeByEmailAndCourseSlugLecture.get();
            return new CourseLearningVm(courseVM, lecture.getLecture().getSection().getId(), lecture.getLecture().getId(), lecture.getWatchingSecond(), LECTURE_TYPE);
        } else if (maxAccessTimeByEmailAndCourseSlugQuiz.isPresent()) {
            var quiz = maxAccessTimeByEmailAndCourseSlugQuiz.get();
            return new CourseLearningVm(courseVM, quiz.getQuiz().getSection().getId(), quiz.getQuiz().getId(), null, QUIZ_TYPE);
        }
        return new CourseLearningVm(courseVM, null ,null, 0, null);
    }

    @Override
    public PageableData<CourseListGetVM> getCoursesByMultiQuery(int pageNum,
                                                         int pageSize,
                                                         String title,
                                                         Float rating,
                                                         String[] level,
                                                         Boolean[] free,
                                                         String categoryName, Integer topicId
    ) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Course> coursePage = courseRepository.findByMultiQuery(pageable, title, rating, level, free, categoryName, topicId);
        List<Course> courses = coursePage.getContent();
        List<CourseListGetVM> courseListGetVMS = courses.stream().map(course -> {
            List<Review> reviews = course.getReviews();
            int ratingCount = reviews.size();
            Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);
            double roundedAverageRating = Math.round(averageRating * 10) / 10.0;
            AtomicInteger totalCurriculumCourse = new AtomicInteger();
            AtomicInteger totalDurationCourse = new AtomicInteger();
            List<Section> sections = sectionService.findByCourseId(course.getId());
            sections.forEach(section -> {
                List<Lecture> lectures = section.getLectures();
                List<Quiz> quizzes = section.getQuizzes();
                totalCurriculumCourse.addAndGet(lectures.size());
                totalCurriculumCourse.addAndGet(quizzes.size());
                int totalSeconds = lectures.stream()
                        .mapToInt(lecture -> lecture.getDuration())
                        .sum();
                totalDurationCourse.addAndGet(totalSeconds);
            });
            double roundedHours = Math.round(totalDurationCourse.get() * 2) / 7200.0;
            String formattedHours = String.format("%.1f hours", roundedHours);
            return CourseListGetVM.fromModel(course, formattedHours, totalCurriculumCourse.get(), roundedAverageRating, ratingCount);
        }).toList();
        return new PageableData(
                pageNum,
                pageSize,
                (int) coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                courseListGetVMS
        );
    }


}
