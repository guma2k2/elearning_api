package com.backend.elearning.domain.questionLecture.studentAnswer;

public record StudentAnswerPostVM(
        Long id,
        String content,
        Long questionLectureId
) {
}
