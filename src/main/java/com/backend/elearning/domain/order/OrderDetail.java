package com.backend.elearning.domain.order;

import com.backend.elearning.domain.course.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private Long price;
}
