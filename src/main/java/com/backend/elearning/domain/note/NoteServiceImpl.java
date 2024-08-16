package com.backend.elearning.domain.note;

import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final NoteRepository noteRepository;

    public NoteServiceImpl(LectureRepository lectureRepository, StudentRepository studentRepository, NoteRepository noteRepository) {
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.noteRepository = noteRepository;
    }


    @Override
    public NoteVM create(NotePostVM notePostVM) {
        Lecture lecture = lectureRepository.findById(notePostVM.lectureId()).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.LECTURE_NOT_FOUND, notePostVM.lectureId()));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.STUDENT_NOT_FOUND, email));

        Note note = Note.builder()
                .lecture(lecture)
                .student(student)
                .content(notePostVM.content())
                .time(notePostVM.second())
                .build();

        return NoteVM.fromModel(noteRepository.saveAndFlush(note), lecture.getId());
    }

    @Override
    public NoteVM update(NotePostVM notePostVM) {
        Note note = noteRepository.findByIdCustom(notePostVM.id()).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.NOTE_NOT_FOUND, notePostVM.id()));
        note.setContent(notePostVM.content());
        note.setTime(notePostVM.second());
        return NoteVM.fromModel(noteRepository.saveAndFlush(note), null);
    }

    @Override
    public void delete(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    @Override
    public List<NoteVM> getBySectionStudent(Long sectionId) {
        List<Note> notes = noteRepository.findBySection(sectionId);
        List<NoteVM> noteVMS = notes.stream().map(note -> NoteVM.fromModel(note, null)).toList();
        return noteVMS;
    }

    @Override
    public List<NoteVM> getAllByCourse(Long courseId) {
        List<Note> notes = noteRepository.findByCourse(courseId);
        List<NoteVM> noteVMS = notes.stream().map(note -> NoteVM.fromModel(note, null)).toList();
        return noteVMS;
    }
}
