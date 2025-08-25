package com.backend.elearning.domain.course;

import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface CourseService {
    PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize, String keyword, CourseStatus status);
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
                                                  List<String> level,
                                                  List<Boolean> free,
                                                  String categoryName,
                                                  Integer topicId
    );

    void delete(Long id);

    void updateStatusCourse(CourseStatusPostVM courseStatusPostVM, Long courseId);

    List<CourseAssignPromotion> getByPromotionId(Long promotionId);

    List<CourseListGetVM> getCoursesByCategory(String categoryName, int pageNum, int pageSize);
}
