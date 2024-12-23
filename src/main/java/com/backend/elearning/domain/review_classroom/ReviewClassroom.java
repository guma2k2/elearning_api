package com.backend.elearning.domain.review_classroom;

import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_classroom")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewClassroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int ratingStar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Builder.Default
    private boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "created_at")
    protected LocalDateTime createdAt;


    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}
