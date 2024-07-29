package com.backend.elearning.domain.learning.learningLecture;


import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LearningLectureService {

    private final LearningLectureRepository learningLectureRepository;

    private final LectureRepository lectureRepository;

    private final StudentRepository studentRepository;


    public LearningLectureService(LearningLectureRepository learningLectureRepository, LectureRepository lectureRepository, StudentRepository studentRepository) {
        this.learningLectureRepository = learningLectureRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
    }


    public void create(LearningLecturePostVM learningLecturePostVM) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<LearningLecture> learningLectureOptional = learningLectureRepository.
                findByEmailAndLectureId(email, learningLecturePostVM.lectureId());
        if (learningLectureOptional.isPresent()) {
            learningLectureOptional.get().setAccessTime(LocalDateTime.now());
            learningLectureOptional.get().setWatchingSecond(learningLecturePostVM.watchingSecond());
            learningLectureOptional.get().setFinished(learningLecturePostVM.finished());
            learningLectureRepository.save(learningLectureOptional.get());
        } else {
            Lecture lecture = lectureRepository.findById(learningLecturePostVM.lectureId()).orElseThrow(
                    () -> new  NotFoundException("")
            );
            Student student = studentRepository.findByEmail(email).orElseThrow(() -> new  NotFoundException(""));
            LearningLecture learningLecture = LearningLecture.builder()
                    .lecture(lecture)
                    .student(student)
                    .accessTime(LocalDateTime.now())
                    .finished(learningLecturePostVM.finished())
                    .watchingSecond(learningLecturePostVM.watchingSecond())
                    .build();
            learningLectureRepository.save(learningLecture);
        }

    }

}
