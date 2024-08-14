package com.backend.elearning.domain.note;

import com.backend.elearning.domain.lecture.Lecture;

public record NoteVM (
        Long id,
        String content,
        String second,
        Long lectureId
) {
    public static NoteVM fromModel(Note note, Long lectureId) {
        String time = note.getTime() + "";
        if (lectureId == null) {
            Lecture lecture = note.getLecture();
            Long lecId = lecture.getId();
            return new NoteVM(note.getId(), note.getContent(), time, lecId);

        }
        return new NoteVM(note.getId(), note.getContent(), time, lectureId);
    }
}
