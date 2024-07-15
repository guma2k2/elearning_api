package com.backend.elearning.domain.order;

import com.backend.elearning.domain.course.CourseListGetVM;

import java.util.List;

public interface OrderDetailService {
    List<CourseListGetVM> getTopCourseBestSeller(int limit);
}
