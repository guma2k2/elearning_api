package com.backend.elearning.domain.order;

import com.backend.elearning.domain.student.Student;
import com.backend.elearning.utils.DateTimeUtils;

import java.util.List;

public record OrderVM(
        Long id,
        String student,
        String coupon,
        String createdAt,
        String status,
        List<OrderDetailVM> orderDetails,
        Long totalPrice
) {
    public static OrderVM fromModel(Order order, List<OrderDetailVM> orderDetails, Long total) {
        String student = order.getStudent().getEmail();
        String coupon = order.getCoupon() != null ? order.getCoupon().getCode() : "No coupon";
        String createdAt = order.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(order.getCreatedAt()) : "";
        return new OrderVM(order.getId(),student, coupon, createdAt, order.getStatus().toString(), orderDetails, total);
    }
}
