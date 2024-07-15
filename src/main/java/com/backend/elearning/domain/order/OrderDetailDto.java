package com.backend.elearning.domain.order;

import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.course.CourseListGetVM;

public record OrderDetailDto(
        Long id,

        CourseGetVM course,

        Double price
) {
    public static OrderDetailDto fromModel(OrderDetail orderDetail, CourseGetVM course) {

        return new OrderDetailDto(orderDetail.getId(),course, orderDetail.getPrice());
    }
}
