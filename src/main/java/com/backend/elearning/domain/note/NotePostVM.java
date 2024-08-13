package com.backend.elearning.domain.note;

public record NotePostVM(
        Long id,
        String content,
        int second,
        Long lectureId
) {
}
