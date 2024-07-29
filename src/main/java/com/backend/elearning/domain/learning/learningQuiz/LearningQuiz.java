package com.backend.elearning.domain.learning.learningQuiz;

import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_quiz")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LearningQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private LocalDateTime accessTime;

    private boolean finished;
}
