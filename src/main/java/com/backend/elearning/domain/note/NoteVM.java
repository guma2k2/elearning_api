package com.backend.elearning.domain.note;

public record NoteVM (
        Long id,
        String content,
        String second
) {
    public static NoteVM fromModel(Note note) {
        String time = note.getTime() + "";
        return new NoteVM(note.getId(), note.getContent(), time);
    }
}
