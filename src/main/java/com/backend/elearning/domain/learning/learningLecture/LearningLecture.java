package com.backend.elearning.domain.learning.learningLecture;

import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_lecture")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LearningLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private LocalDateTime accessTime;

    @Builder.Default
    private int watchingSecond = 0;

    private boolean finished;

}
