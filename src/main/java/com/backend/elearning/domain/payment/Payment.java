package com.backend.elearning.domain.payment;

import com.backend.elearning.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long amount;
    private String bankCode ;
    private String bankTranNo ;
    private String cartType ;
    private String payDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}