package com.backend.elearning.domain.learning.learningQuiz;


import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LearningQuizService {

    private final LearningQuizRepository learningQuizRepository;
    private final StudentRepository studentRepository;

    private final QuizRepository quizRepository;

    public LearningQuizService(LearningQuizRepository learningQuizRepository, StudentRepository studentRepository, QuizRepository quizRepository) {
        this.learningQuizRepository = learningQuizRepository;
        this.studentRepository = studentRepository;
        this.quizRepository = quizRepository;
    }

    public void create(LearningQuizPostVM learningQuizPostVM) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<LearningQuiz> learningQuizOptional = learningQuizRepository.findByEmailAndQuizId(email, learningQuizPostVM.quizId());
        if (learningQuizOptional.isPresent()) {
            learningQuizOptional.get().setAccessTime(LocalDateTime.now());
            learningQuizOptional.get().setFinished(learningQuizPostVM.finished());
            learningQuizRepository.save(learningQuizOptional.get());
        } else {
            Quiz quiz = quizRepository.findById(learningQuizPostVM.quizId()).orElseThrow();
            Student student = studentRepository.findByEmail(email).orElseThrow();
             LearningQuiz learningQuiz= LearningQuiz.builder()
                    .quiz(quiz)
                    .student(student)
                    .accessTime(LocalDateTime.now())
                    .finished(learningQuizPostVM.finished())
                    .build();
            learningQuizRepository.save(learningQuiz);
        }
    }

}
