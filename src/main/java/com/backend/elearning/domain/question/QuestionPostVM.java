package com.backend.elearning.domain.question;

import com.backend.elearning.domain.answer.AnswerVM;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionPostVM(
        Long id,
        @NotNull(message = "question must not be null")
        String title,
        Long quizId,
        List<AnswerVM> answers
) {
}
