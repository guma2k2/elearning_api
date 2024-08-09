package com.backend.elearning.domain.course;

import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface CourseService {
    PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize, String keyword);
    CourseVM create(CoursePostVM coursePostVM);

    CourseVM update(CoursePostVM coursePostVM, Long userId, Long courseId);

    CourseVM getCourseById(Long id);

    CourseListGetVM getCourseListGetVMById(Long id);

    CourseLearningVm getCourseBySlug(String slug);

    List<CourseListGetVM> getByUserId(Long userId);

    PageableData<CourseListGetVM> getCoursesByMultiQuery(int pageNum,
                                                  int pageSize,
                                                  String title,
                                                  Float rating,
                                                  String[] level,
                                                  Boolean[] free,
                                                  String categoryName,
                                                  Integer topicId
    );

    List<CourseListGetVM> getCoursesByCategoryId(Integer categoryId);

    void delete(Long id);

    void updateStatusCourse(boolean status, Long courseId);
}
