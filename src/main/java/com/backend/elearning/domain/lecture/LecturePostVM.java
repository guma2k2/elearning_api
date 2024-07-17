package com.backend.elearning.domain.lecture;

public record LecturePostVM(
        Long id,
        String title,
        float number,
        String lectureDetails,
        int duration,
        String video,
        Long sectionId
) {
}
