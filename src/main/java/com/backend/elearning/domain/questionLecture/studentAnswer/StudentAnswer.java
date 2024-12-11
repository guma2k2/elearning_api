package com.backend.elearning.domain.questionLecture.studentAnswer;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.questionLecture.QuestionLecture;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentAnswer extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_lecture_id")
    private QuestionLecture questionLecture;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
}
