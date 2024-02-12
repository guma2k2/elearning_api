package com.backend.elearning.domain.question;

import com.backend.elearning.domain.answer.AnswerVM;

import java.util.List;

public record QuestionVM(
        Long id,
        String title,
        List<AnswerVM> answers
) {
    public static QuestionVM fromModel (Question question, List<AnswerVM> answerVMS) {
        return new QuestionVM(question.getId(), question.getTitle(), answerVMS);
    }
}
