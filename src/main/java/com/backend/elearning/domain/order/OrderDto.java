package com.backend.elearning.domain.order;

import java.util.List;

public record OrderDto(
        Long id,
        String createdAt,
        String status,
        List<OrderDetailDto> orderDetails
) {
    public static OrderDto fromModel(Order order, List<OrderDetailDto> orderDetails) {
        return new OrderDto(order.getId(), order.getCreatedAt().toString(), order.getStatus().toString(), orderDetails);
    }
}
