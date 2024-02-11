package com.backend.elearning.domain.question;

import com.backend.elearning.domain.answer.AnswerVM;

import java.util.List;

public record QuestionPostVM(
        Long id,
        String title,
        Long quizId,
        List<AnswerVM> answers
) {
}
