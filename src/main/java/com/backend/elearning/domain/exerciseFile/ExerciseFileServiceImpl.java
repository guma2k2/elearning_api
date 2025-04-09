package com.backend.elearning.domain.exerciseFile;

import com.backend.elearning.domain.excercise.Exercise;
import com.backend.elearning.domain.excercise.ExerciseRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.utils.DateTimeUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExerciseFileServiceImpl implements ExerciseFileService {

    private final ExerciseFileRepo exerciseFileRepo;

    private final ExerciseRepository exerciseRepository;

   private final StudentRepository studentRepository;

    public ExerciseFileServiceImpl(ExerciseFileRepo exerciseFileRepo, ExerciseRepository exerciseRepository, StudentRepository studentRepository) {
        this.exerciseFileRepo = exerciseFileRepo;
        this.exerciseRepository = exerciseRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public ExerciseFileVM create(ExerciseFilePostVM exerciseFilePostVM) {
        Exercise exercise = exerciseRepository.findById(exerciseFilePostVM.exerciseId()).orElseThrow();
        ExerciseFile exerciseFile = ExerciseFile.builder()
                .fileName(exerciseFilePostVM.fileName())
                .fileUrl(exerciseFilePostVM.fileUrl())
                .exercise(exercise)
                .build();
        ExerciseFile savedExerciseFile = exerciseFileRepo.saveAndFlush(exerciseFile);
        return ExerciseFileVM.fromModel(savedExerciseFile);
    }

    @Override
    public ExerciseFileVM update(ExerciseFilePostVM exerciseFilePostVM, Long id) {
        ExerciseFile exerciseFile = exerciseFileRepo.findById(id).orElseThrow();
        exerciseFile.setFileName(exerciseFilePostVM.fileName());
        exerciseFile.setFileUrl(exerciseFilePostVM.fileUrl());
        ExerciseFile savedExerciseFile = exerciseFileRepo.saveAndFlush(exerciseFile);
        return ExerciseFileVM.fromModel(savedExerciseFile);
    }

    @Override
    public List<ExerciseFileVM> getByExerciseId(Long exerciseId) {
        List<ExerciseFile> exerciseFiles = exerciseFileRepo.findByExercise(exerciseId);
        return exerciseFiles.stream().map(exerciseFile -> ExerciseFileVM.fromModel(exerciseFile)).toList();
    }

    @Override
    public void delete(Long referenceFileId) {
        exerciseFileRepo.deleteById(referenceFileId);
    }
}
