package com.backend.elearning.domain.questionLecture.userAnswer;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.questionLecture.QuestionLecture;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserAnswer extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_lecture_id")
    private QuestionLecture questionLecture;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
