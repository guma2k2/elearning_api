package com.backend.elearning.domain.course;

import com.backend.elearning.domain.common.PageableData;

public interface CourseService {
    PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize);
    CourseVM create(CoursePostVM coursePostVM);

    CourseVM update(CoursePostVM coursePostVM, Long userId, Long courseId);

    CourseVM getCourseById(Long id);

    PageableData<CourseVM> getCoursesByMultiQuery(int pageNum, int pageSize, String name, int rating);
}
