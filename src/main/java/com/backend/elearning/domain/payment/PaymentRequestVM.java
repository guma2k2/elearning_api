package com.backend.elearning.domain.payment;

public record PaymentRequestVM(
        int amount,
        String bankCode,
        Long orderId
) {
}
