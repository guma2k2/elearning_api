package com.backend.elearning.domain.question;

import com.backend.elearning.domain.answer.AnswerVM;

import java.util.List;

public record QuestionVM(
        Long id,
        String title,
        List<AnswerVM> answers
) {
}
