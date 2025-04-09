package com.backend.elearning.domain.studentExercise;

import java.util.List;

public interface StudentExerciseService {

    StudentExerciseVM create(StudentExercisePostVM referencePostVM);

    StudentExerciseVM update(StudentExercisePostVM referencePostVM, Long referenceId);

    void delete(Long referenceId);

    StudentExerciseVM getByExerciseId (Long exerciseId);

    List<StudentExerciseGetVM> getListByExerciseId (Long exerciseId);

}
