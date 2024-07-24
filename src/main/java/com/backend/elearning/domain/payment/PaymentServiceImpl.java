package com.backend.elearning.domain.payment;

import com.backend.elearning.configuration.VNPayConfig;
import com.backend.elearning.domain.order.Order;
import com.backend.elearning.domain.order.OrderRepository;
import com.backend.elearning.utils.DateTimeUtils;
import com.backend.elearning.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final VNPayConfig vnPayConfig;

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;
    @Override
    public PaymentVM.VNPayResponse createVNPayPayment(PaymentRequestVM request, HttpServletRequest httpServletRequest) {
        long amount = request.amount() * 100L;
        String bankCode = request.bankCode();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(request);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(httpServletRequest));
        //build query url
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        queryUrl += "&vnp_SecureHash=" + VNPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentVM.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    @Override
    public void savePayment(PaymentPostVM request) {
        Order order = orderRepository.findById(request.orderId()).orElseThrow();
        Payment payment = Payment.builder()
                .bankTranNo(request.bankTranNo())
                .payDate(DateTimeUtils.convertStringToLocalDateTime(request.payDate(), DateTimeUtils.PAYMENT_TYPE))
                .amount(request.amount())
                .cartType(request.cartType())
                .bankCode(request.bankCode())
                .order(order)
                .build();
        paymentRepository.save(payment);
        // Todo : update order Status to success
    }
}
