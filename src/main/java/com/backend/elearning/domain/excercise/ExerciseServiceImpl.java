package com.backend.elearning.domain.excercise;

import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.domain.classroom.ClassroomRepository;
import com.backend.elearning.domain.exerciseFile.ExerciseFileService;
import com.backend.elearning.domain.exerciseFile.ExerciseFileVM;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final ClassroomRepository classroomRepository;

    private final ExerciseFileService exerciseFileService;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ClassroomRepository classroomRepository, ExerciseFileService exerciseFileService) {
        this.exerciseRepository = exerciseRepository;
        this.classroomRepository = classroomRepository;
        this.exerciseFileService = exerciseFileService;
    }

    @Override
    public ExerciseVM create(ExercisePostVM exercisePostVM) {
        Classroom classroom = classroomRepository.findById(exercisePostVM.classroomId()).orElseThrow();
        LocalDateTime deadline = DateTimeUtils.convertStringToLocalDateTime(exercisePostVM.deadline(), DateTimeUtils.NORMAL_TYPE);

        Exercise exercise = Exercise.builder()
                .title(exercisePostVM.title())
                .submission_deadline(deadline)
                .description(exercisePostVM.description())
                .classroom(classroom)
                .build();

        Exercise savedExercise = exerciseRepository.saveAndFlush(exercise);
        return ExerciseVM.fromModel(savedExercise);
    }

    @Override
    public ExerciseVM update(ExercisePostVM exercisePostVM, Long referenceId) {
        LocalDateTime deadline = DateTimeUtils.convertStringToLocalDateTime(exercisePostVM.deadline(), DateTimeUtils.NORMAL_TYPE);
        Exercise exercise = exerciseRepository.findById(referenceId).orElseThrow();
        exercise.setTitle(exercisePostVM.title());
        exercise.setDescription(exercisePostVM.description());
        exercise.setSubmission_deadline(deadline);
        Exercise savedExercise = exerciseRepository.saveAndFlush(exercise);
        return ExerciseVM.fromModel(savedExercise);
    }

    @Override
    public void delete(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow();
        if (exercise.getExerciseFiles().size() > 0) {
            throw new BadRequestException("Bài tập này có tài liệu");
        }
        exerciseRepository.deleteById(exerciseId);
    }

    @Override
    public ExerciseDetailVM getById(Long id) {
        Exercise exercise = exerciseRepository.findByIdCustom(id).orElseThrow();
        List<ExerciseFileVM> fileVMS = exerciseFileService.getByExerciseId(id   );
        return ExerciseDetailVM.fromModel(exercise, fileVMS);
    }
}
