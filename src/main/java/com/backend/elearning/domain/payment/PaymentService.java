package com.backend.elearning.domain.payment;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentVM.VNPayResponse createVNPayPayment(PaymentRequestVM request, HttpServletRequest httpServletRequest);

    void savePayment(PaymentPostVM request);
}
