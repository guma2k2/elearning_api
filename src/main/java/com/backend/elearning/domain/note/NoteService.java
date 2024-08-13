package com.backend.elearning.domain.note;

import com.backend.elearning.domain.lecture.LecturePostVM;
import com.backend.elearning.domain.lecture.LectureVm;

import java.util.List;

public interface NoteService {
    NoteVM create(NotePostVM notePostVM);
    NoteVM update(NotePostVM notePostVM);
    void delete(Long noteId);
    List<NoteVM> getBySectionStudent(Long sectionId);
    List<NoteVM> getAllByCourse(Long courseId);
}
