package com.backend.elearning.domain.questionLecture;


import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.order.OrderDetail;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswer;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswer;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "question_lecture")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionLecture extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "questionLecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAnswer> userAnswers = new ArrayList<>();


    @OneToMany(mappedBy = "questionLecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();
}
