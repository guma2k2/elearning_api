package com.backend.elearning.domain.learning.learningLecture;

public record LearningLecturePostVM(
        Long lectureId,
        int watchingSecond,
        boolean finished
) {
}
