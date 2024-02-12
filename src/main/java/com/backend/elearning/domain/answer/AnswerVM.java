package com.backend.elearning.domain.answer;

public record AnswerVM(
         Long id,
         String answerText,
         String reason,
         boolean correct
) {
    public static AnswerVM fromModel(Answer answer) {
        return new AnswerVM(answer.getId(), answer.getAnswerText(), answer.getReason(), answer.isCorrect());
    }
}
