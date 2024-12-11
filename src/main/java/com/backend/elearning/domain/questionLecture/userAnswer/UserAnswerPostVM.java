package com.backend.elearning.domain.questionLecture.userAnswer;

public record UserAnswerPostVM(
        String content,
        Long questionLectureId
) {
}
