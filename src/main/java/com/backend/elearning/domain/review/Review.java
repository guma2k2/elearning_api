package com.backend.elearning.domain.review;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int ratingStar;

    private String reasonRefused;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

}
