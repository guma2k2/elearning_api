package com.backend.elearning.domain.order;

import java.util.List;

public record OrderVM(
        Long id,
        String createdAt,
        String status,
        List<OrderDetailVM> orderDetails
) {
    public static OrderVM fromModel(Order order, List<OrderDetailVM> orderDetails) {
        return new OrderVM(order.getId(), order.getCreatedAt().toString(), order.getStatus().toString(), orderDetails);
    }
}
