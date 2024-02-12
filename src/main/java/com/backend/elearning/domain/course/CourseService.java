package com.backend.elearning.domain.course;

public interface CourseService {
    CourseVM create(CoursePostVM coursePostVM, Long userId);

    CourseVM update(CoursePostVM coursePostVM, Long userId, Long courseId);

    CourseVM getCourseById(Long id);
}
