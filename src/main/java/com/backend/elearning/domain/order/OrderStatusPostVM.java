package com.backend.elearning.domain.order;

public record OrderStatusPostVM (
        EOrderStatus status,
        String reason
) {
}
