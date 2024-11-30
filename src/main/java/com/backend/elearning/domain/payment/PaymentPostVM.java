package com.backend.elearning.domain.payment;

public record PaymentPostVM(

         long amount,
         String bankCode ,
         String bankTranNo ,
         String cardType ,
         String payDate,
         Long orderId
) {
}
