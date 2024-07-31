package com.backend.elearning.domain.course;

import com.backend.elearning.domain.common.PageableData;

public interface CourseService {
    PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize);
    CourseVM create(CoursePostVM coursePostVM);

    CourseVM update(CoursePostVM coursePostVM, Long userId, Long courseId);

    CourseVM getCourseById(Long id);

    CourseListGetVM getCourseListGetVMById(Long id);

    CourseLearningVm getCourseBySlug(String slug);

    PageableData<CourseListGetVM> getCoursesByMultiQuery(int pageNum,
                                                  int pageSize,
                                                  String title,
                                                  Float rating,
                                                  String[] level,
                                                  Boolean[] free,
                                                  String categoryName,
                                                  Integer topicId
    );
}
