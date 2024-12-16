package com.backend.elearning.domain.lecture;

public record LectureGetVM(
        Long id,
        String title,
        Long sectionId
) {

    public static LectureGetVM fromModel(Lecture lecture) {
        return new LectureGetVM(lecture.getId(), lecture.getTitle(), lecture.getSection().getId());
    }
}
