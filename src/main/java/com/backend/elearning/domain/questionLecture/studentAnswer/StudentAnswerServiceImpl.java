package com.backend.elearning.domain.questionLecture.studentAnswer;


import com.backend.elearning.domain.questionLecture.AnswerLecture;
import com.backend.elearning.domain.questionLecture.QuestionLecture;
import com.backend.elearning.domain.questionLecture.QuestionLectureRepo;
import com.backend.elearning.domain.questionLecture.QuestionLectureVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentAnswerServiceImpl implements StudentAnswerService {

    private final StudentAnswerRepo studentAnswerRepo;

    private final StudentRepository studentRepository;
    private final QuestionLectureRepo questionLectureRepo;

    public StudentAnswerServiceImpl(StudentAnswerRepo studentAnswerRepo, StudentRepository studentRepository, QuestionLectureRepo questionLectureRepo) {
        this.studentAnswerRepo = studentAnswerRepo;
        this.studentRepository = studentRepository;
        this.questionLectureRepo = questionLectureRepo;
    }

    @Override
    public AnswerLecture create(StudentAnswerPostVM studentAnswerPostVM) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        QuestionLecture questionLecture = questionLectureRepo.findById(studentAnswerPostVM.questionLectureId()).orElseThrow();
        Student student = studentRepository.findByEmail(email).orElseThrow();
        StudentAnswer studentAnswer = StudentAnswer
                .builder()
                .content(studentAnswerPostVM.content())
                .student(student)
                .questionLecture(questionLecture)
                .build();
        studentAnswer.setCreatedAt(LocalDateTime.now());
        studentAnswer.setUpdatedAt(LocalDateTime.now());

        StudentAnswer savedStudentAnswer = studentAnswerRepo.saveAndFlush(studentAnswer);
        return AnswerLecture.fromModelStudent(savedStudentAnswer);
    }
}
