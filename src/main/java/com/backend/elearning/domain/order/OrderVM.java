package com.backend.elearning.domain.order;

import com.backend.elearning.domain.student.Student;

import java.util.List;

public record OrderVM(
        Long id,
        String student,
        String coupon,
        String createdAt,
        String status,
        List<OrderDetailVM> orderDetails,
        Double totalPrice
) {
    public static OrderVM fromModel(Order order, List<OrderDetailVM> orderDetails, Double total) {
        String student = order.getStudent().getEmail();
        String coupon = order.getCoupon() != null ? order.getCoupon().getCode() : "No coupon";
        return new OrderVM(order.getId(),student, coupon, order.getCreatedAt().toString(), order.getStatus().toString(), orderDetails, total);
    }
}
