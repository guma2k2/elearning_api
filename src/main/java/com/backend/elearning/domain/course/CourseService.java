package com.backend.elearning.domain.course;

public interface CourseService {
    CourseVM create(CoursePostVM coursePostVM, Long userId);

    CourseVM getCourseById(Long id);
}
