package com.backend.elearning.domain.cart;

import com.backend.elearning.domain.course.CourseListGetVM;

public record CartListGetVM(
        CourseListGetVM course,
        boolean buyLater
) {
}
