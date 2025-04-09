package com.backend.elearning.domain.exerciseFile;


import com.backend.elearning.domain.referencefile.ReferenceFilePostVM;
import com.backend.elearning.domain.referencefile.ReferenceFileVM;

import java.util.List;

public interface ExerciseFileService {
    ExerciseFileVM create(ExerciseFilePostVM exerciseFilePostVM);

    ExerciseFileVM update(ExerciseFilePostVM exerciseFilePostVM, Long id);
    List<ExerciseFileVM> getByExerciseId(Long exerciseId);

    void delete(Long exerciseFileId);
}
