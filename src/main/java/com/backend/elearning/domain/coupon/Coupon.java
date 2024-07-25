package com.backend.elearning.domain.coupon;
import com.backend.elearning.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int discountPercent;

    @Column(unique = true)
    private String code;

    private LocalDateTime startTime;
    private LocalDateTime endTime;


    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

}
