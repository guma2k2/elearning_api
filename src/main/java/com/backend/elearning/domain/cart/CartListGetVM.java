package com.backend.elearning.domain.cart;

import com.backend.elearning.domain.course.CourseListGetVM;

public record CartListGetVM(
        String fullName,
        CourseListGetVM course,
        boolean buyLater
) {
}
