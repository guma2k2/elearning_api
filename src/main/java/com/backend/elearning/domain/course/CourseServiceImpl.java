package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    private final TopicRepository topicRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
    }


    @Override
    public Course save(CoursePostVM coursePostVM) {
        Category category = categoryRepository.findById(coursePostVM.categoryId()).orElseThrow();
        Topic topic = topicRepository.findById(coursePostVM.topicId()).orElseThrow();
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
                .build();

        return courseRepository.save(course);
    }

    @Override
    public CourseVM getCourseById(Long id) {
        return null;
    }
}
