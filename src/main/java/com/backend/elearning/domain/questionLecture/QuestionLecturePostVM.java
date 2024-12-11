package com.backend.elearning.domain.questionLecture;

public record QuestionLecturePostVM (
        String title,
        String description,
        Long lectureId
) {
}
