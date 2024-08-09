package com.backend.elearning.domain.payment;

import com.backend.elearning.configuration.VNPayConfig;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.learning.learningCourse.LearningCourse;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.order.Order;
import com.backend.elearning.domain.order.OrderDetail;
import com.backend.elearning.domain.order.OrderDetailRepository;
import com.backend.elearning.domain.order.OrderRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.DateTimeUtils;
import com.backend.elearning.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final VNPayConfig vnPayConfig;

    private final PaymentRepository paymentRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final StudentRepository studentRepository;
    private final OrderRepository orderRepository;

    private final LearningCourseRepository learningCourseRepository;
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
        Order order = orderRepository.findById(request.orderId()).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.ORDER_NOT_FOUND, request.orderId()));
        Payment payment = Payment.builder()
                .bankTranNo(request.bankTranNo())
                .payDate(DateTimeUtils.convertStringToLocalDateTime(request.payDate(), DateTimeUtils.PAYMENT_TYPE))
                .amount(request.amount())
                .cartType(request.cartType())
                .bankCode(request.bankCode())
                .order(order)
                .build();
        paymentRepository.save(payment);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null) {
            for (OrderDetail od: orderDetails) {
                Course course = od.getCourse();
                Optional<LearningCourse> learningCourseOptional = learningCourseRepository.findByStudentAndCourse(email, course.getId());
                if (!learningCourseOptional.isPresent()) {
                    Student student = studentRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.STUDENT_NOT_FOUND, email));
                    LearningCourse learningCourse = LearningCourse.builder()
                            .student(student)
                            .course(course)
                            .build();
                    learningCourseRepository.save(learningCourse);
                }
            }
        }
    }
}
