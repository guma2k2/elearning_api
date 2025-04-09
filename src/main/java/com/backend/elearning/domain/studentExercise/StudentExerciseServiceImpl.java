package com.backend.elearning.domain.studentExercise;

import com.backend.elearning.domain.excercise.Exercise;
import com.backend.elearning.domain.excercise.ExerciseRepository;
import com.backend.elearning.domain.excercise.ExerciseVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.utils.DateTimeUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentExerciseServiceImpl implements StudentExerciseService {

    private final StudentExerciseRepository studentExerciseRepository;

    private final ExerciseRepository exerciseRepository;

    private final StudentRepository studentRepository;

    public StudentExerciseServiceImpl(StudentExerciseRepository studentExerciseRepository, ExerciseRepository exerciseRepository, StudentRepository studentRepository) {
        this.studentExerciseRepository = studentExerciseRepository;
        this.exerciseRepository = exerciseRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public StudentExerciseVM create(StudentExercisePostVM studentExercisePostVM) {
        Exercise exercise = exerciseRepository.findById(studentExercisePostVM.exerciseId()).orElseThrow();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow();

        StudentExercise studentExercise = StudentExercise.builder()
                .student(student)
                .submitted(studentExercisePostVM.submitted())
                .fileUrl(studentExercisePostVM.fileUrl())
                .fileName(studentExercisePostVM.fileName())
                .exercise(exercise)
                .build();
        if (studentExercisePostVM.submitted()) {
            studentExercise.setSubmittedTime(LocalDateTime.now());
        }

        StudentExercise savedStudentExercise = studentExerciseRepository.saveAndFlush(studentExercise);
        return StudentExerciseVM.fromModel(savedStudentExercise);
    }

    @Override
    public StudentExerciseVM update(StudentExercisePostVM studentExercisePostVM, Long id) {
        StudentExercise studentExercise = studentExerciseRepository.findById(id).orElseThrow();
        studentExercise.setFileName(studentExercisePostVM.fileName());
        studentExercise.setFileUrl(studentExercisePostVM.fileUrl());
        studentExercise.setSubmitted(studentExercisePostVM.submitted());
        if (studentExercisePostVM.submitted()) {
            studentExercise.setSubmittedTime(LocalDateTime.now());
        }
        StudentExercise savedStudentExercise = studentExerciseRepository.saveAndFlush(studentExercise);
        return StudentExerciseVM.fromModel(savedStudentExercise);
    }

    @Override
    public void delete(Long exerciseId) {
        studentExerciseRepository.deleteById(exerciseId);
    }

    @Override
    public StudentExerciseVM getByExerciseId(Long exerciseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<StudentExercise> studentExerciseOptional = studentExerciseRepository.findByExerciseIdAndStudent(email, exerciseId);
        if (studentExerciseOptional.isPresent()) {
            StudentExercise studentExercise = studentExerciseOptional.get();
            return StudentExerciseVM.fromModel(studentExercise);
        }
        return null;
    }

    @Override
    public List<StudentExerciseGetVM> getListByExerciseId(Long exerciseId) {
        List<StudentExercise> studentExercises = studentExerciseRepository.findByExerciseId(exerciseId);
        List<StudentExerciseGetVM> studentExerciseVMS = studentExercises.stream().map(StudentExerciseGetVM::fromModel).toList();
        return studentExerciseVMS;
    }
}
