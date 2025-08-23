package com.backend.elearning.domain.promotion;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.course.Course;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promotion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Promotion extends AbstractAuditEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private int discountPercent;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToMany(mappedBy = "promotions")
    private Set<Course> courses = new HashSet<>();
}
