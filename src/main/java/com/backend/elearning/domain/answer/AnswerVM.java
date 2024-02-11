package com.backend.elearning.domain.answer;

public record AnswerVM(
         Long id,
         String answerText,
         String reason,
         boolean correct
) {
}
