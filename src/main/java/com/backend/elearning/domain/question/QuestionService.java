package com.backend.elearning.domain.question;


import java.util.List;

public interface QuestionService {
    QuestionVM create (QuestionPostVM questionPostVM);

    QuestionVM update (QuestionPostVM questionPostVM, Long questionId);
    List<QuestionVM> getByQuizId (Long quizId);

    QuestionVM getById (Long questionId);
}
