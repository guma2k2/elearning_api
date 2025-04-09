package com.backend.elearning.domain.excercise;

public interface ExerciseService {

    ExerciseVM create(ExercisePostVM referencePostVM);

    ExerciseVM update(ExercisePostVM referencePostVM, Long referenceId);

    void delete(Long referenceId);

    ExerciseDetailVM getById(Long id);

}
