package com.backend.elearning.domain.questionLecture.studentAnswer;

public record StudentAnswerPostVM(
        String content,
        Long questionLectureId
) {
}
