package com.backend.elearning.domain.cart;

import com.backend.elearning.domain.course.CourseListGetVM;

public record CartListGetVM(
        Long id,
        CourseListGetVM course,
        boolean buyLater
) {
}
