package com.backend.elearning.domain.questionLecture.userAnswer;

public record UserAnswerPostVM(
        Long id,
        String content,
        Long questionLectureId
) {
}
