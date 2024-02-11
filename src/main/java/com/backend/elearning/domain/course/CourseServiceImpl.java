package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.media.MediaService;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    private final MediaService mediaService;

    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository, UserRepository userRepository, MediaService mediaService) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.mediaService = mediaService;
    }


    @Override
    public CourseVM create(CoursePostVM coursePostVM, Long userId) {
        if (courseRepository.countExistByTitle(coursePostVM.title(), null) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.COURSE_TITLE_DUPLICATED);
        }
        String imageURL = mediaService.getUrlById(coursePostVM.imageId());
        Category category = categoryRepository.findById(coursePostVM.categoryId()).orElseThrow();
        Topic topic = topicRepository.findById(coursePostVM.topicId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Course course = Course.builder()
                .title(coursePostVM.title())
                .level(ELevel.valueOf(coursePostVM.level()))
                .description(coursePostVM.description())
                .imageId(coursePostVM.imageId())
                .headline(coursePostVM.headline())
                .objective(coursePostVM.objective())
                .requirement(coursePostVM.requirement())
                .targetAudience(coursePostVM.targetAudience())
                .free(coursePostVM.free())
                .category(category)
                .topic(topic)
                .user(user)
                .build();

        return CourseVM.fromModel(courseRepository.save(course), imageURL, new ArrayList<>());
    }

    @Override
    public CourseVM getCourseById(Long id) {
        return null;
    }
}
