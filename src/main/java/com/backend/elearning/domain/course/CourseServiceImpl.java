package com.backend.elearning.domain.course;

import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course save(CoursePostVM coursePostVM) {
        return null;
    }

    @Override
    public CourseVM getCourseById(Long id) {
        return null;
    }
}
