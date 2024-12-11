package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuestionLectureServiceImpl implements QuestionLectureService {

    private final QuestionLectureRepo questionLectureRepo;
    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    public QuestionLectureServiceImpl(QuestionLectureRepo questionLectureRepo, StudentRepository studentRepository, LectureRepository lectureRepository) {
        this.questionLectureRepo = questionLectureRepo;
        this.studentRepository = studentRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    public QuestionLectureVM create(QuestionLecturePostVM questionLecturePostVM) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Lecture lecture = lectureRepository.findById(questionLecturePostVM.lectureId()).orElseThrow();
        Student student = studentRepository.findByEmail(email).orElseThrow();
        QuestionLecture questionLecture = QuestionLecture
                .builder()
                .title(questionLecturePostVM.title())
                .description(questionLecturePostVM.description())
                .lecture(lecture)
                .student(student)
                .build();
        questionLecture.setCreatedAt(LocalDateTime.now());
        questionLecture.setUpdatedAt(LocalDateTime.now());

        QuestionLecture savedQuestionLecture = questionLectureRepo.saveAndFlush(questionLecture);
        return QuestionLectureVM.fromModel(savedQuestionLecture);
    }
}
