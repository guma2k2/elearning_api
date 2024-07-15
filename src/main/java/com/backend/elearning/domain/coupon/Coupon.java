package com.backend.elearning.domain.coupon;
import jakarta.persistence.*;
import lombok.*;

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
}
