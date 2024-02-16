package com.backend.elearning.domain.quiz;

public interface QuizService {
    QuizVM create(QuizPostVM quizPostVM);
    QuizVM update(QuizPostVM quizPutVM, Long quizId);
}
