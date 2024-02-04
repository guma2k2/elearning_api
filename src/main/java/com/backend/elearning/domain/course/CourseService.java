package com.backend.elearning.domain.course;

public interface CourseService {
    Course save(CoursePostVM coursePostVM);

    CourseVM getCourseById(Long id);
}
