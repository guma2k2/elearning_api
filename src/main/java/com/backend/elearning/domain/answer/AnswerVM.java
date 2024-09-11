package com.backend.elearning.domain.answer;

import jakarta.validation.constraints.NotNull;

public record AnswerVM(
         Long id,
         @NotNull(message = "answer test must not be null")
         String answerText,
         String reason,
         boolean correct
) {
    public static AnswerVM fromModel(Answer answer) {
        return new AnswerVM(answer.getId(), answer.getAnswerText(), answer.getReason(), answer.isCorrect());
    }
}
