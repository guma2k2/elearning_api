package com.backend.elearning.domain.payment;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/vn-pay")
    public ResponseEntity<PaymentVM.VNPayResponse> pay(@RequestBody PaymentRequestVM request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(paymentService.createVNPayPayment(request, httpServletRequest));
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentVM.VNPayResponse> payCallbackHandler(@RequestParam String vnp_ResponseCode) {
        if (vnp_ResponseCode.equals("00")) {
            return new ResponseEntity<>(new PaymentVM.VNPayResponse("00", "Success", ""), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/vn-pay-success")
    public ResponseEntity<Void> pay(@RequestBody PaymentPostVM request) {
        paymentService.savePayment(request);
        return ResponseEntity.ok().build();
    }
}
