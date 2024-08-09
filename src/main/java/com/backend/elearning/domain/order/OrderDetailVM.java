package com.backend.elearning.domain.order;

import com.backend.elearning.domain.course.CourseGetVM;

public record OrderDetailVM(
        Long id,

        CourseGetVM course,

        Long price
) {
    public static OrderDetailVM fromModel(OrderDetail orderDetail, CourseGetVM course) {

        return new OrderDetailVM(orderDetail.getId(),course, orderDetail.getPrice());
    }
}
