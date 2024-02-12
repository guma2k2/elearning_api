package com.backend.elearning.domain.question;


import java.util.List;

public interface QuestionService {
    void create (QuestionPostVM questionPostVM);
    List<QuestionVM> getByQuizId (Long quizId);
}
