package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.media.MediaService;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionService;
import com.backend.elearning.domain.section.SectionVM;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final SectionService sectionService;
    private final MediaService mediaService;

    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository, UserRepository userRepository, SectionService sectionService, MediaService mediaService) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.sectionService = sectionService;
        this.mediaService = mediaService;
    }


    @Override
    public PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize) {
        List<CourseVM> courseVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Course> coursePage = courseRepository.findAll(pageable);
        List<Course> courses = coursePage.getContent();
        for (Course course : courses) {
            // Todo: get url image of course by imageId
            courseVMS.add(CourseVM.fromModel(course,"" ,new ArrayList<>()));
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
    public CourseVM create(CoursePostVM coursePostVM, Long userId) {
        if (courseRepository.countExistByTitle(coursePostVM.title(), null) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.COURSE_TITLE_DUPLICATED);
        }
        Category category = categoryRepository.findById(coursePostVM.categoryId()).orElseThrow();
        Topic topic = topicRepository.findById(coursePostVM.topicId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Course course = Course.builder()
                .title(coursePostVM.title())
                .category(category)
                .topic(topic)
                .user(user)
                .build();
        return CourseVM.fromModel(courseRepository.save(course), null, new ArrayList<>());
    }

    @Override
    public CourseVM update(CoursePostVM coursePutVM, Long userId, Long courseId) {
        String imageURL = mediaService.getUrlById(coursePutVM.imageId());
        Course oldCourse = courseRepository.findByIdReturnSections(courseId).orElseThrow();
        if (oldCourse.getUser().getId() != userId) {
            throw new BadRequestException("You don't have permission to edit this course");
        }
        if (courseRepository.countExistByTitle(coursePutVM.title(), courseId) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.COURSE_TITLE_DUPLICATED);
        }
        if (oldCourse.getCategory().getId() != coursePutVM.categoryId()) {
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
        oldCourse.setImageId(coursePutVM.imageId());
        oldCourse.setLevel(ELevel.valueOf(coursePutVM.level()));

        List<SectionVM> sections = oldCourse.getSections()
                .stream().map(section -> sectionService.getById(section.getId())).toList();

        // Todo : list all sections by course id -> done
        return CourseVM.fromModel(courseRepository.save(oldCourse), imageURL, sections);
    }

    @Override
    public CourseVM getCourseById(Long id) {
        Course course = courseRepository.findByIdReturnSections(id).orElseThrow();
        log.info(String.valueOf(course.getSections().size()));
        List<SectionVM> sections = new ArrayList<>(course.getSections()
                .stream().map(section -> sectionService.getById(section.getId())).toList());
        sections.sort(Comparator.comparing(SectionVM::number));
        return CourseVM.fromModel(course, "", sections);
    }
}
