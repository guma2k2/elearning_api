package com.backend.elearning.domain.coupon;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

}
