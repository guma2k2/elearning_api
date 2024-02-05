package com.backend.elearning.domain.lecture;

public record LecturePostVM(
        Long id,
        String title,
        int number,
        String lectureDetails,
        String videoId,
        Long sectionId
) {
}
