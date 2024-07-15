package com.backend.elearning.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;

public class PaymentVM {
    @Builder
    @AllArgsConstructor
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
