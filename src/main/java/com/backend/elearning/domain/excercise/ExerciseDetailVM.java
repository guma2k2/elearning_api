package com.backend.elearning.domain.excercise;

import com.backend.elearning.domain.classroom.ClassroomVM;
import com.backend.elearning.domain.exerciseFile.ExerciseFileVM;
import com.backend.elearning.utils.DateTimeUtils;

import java.util.List;

public record ExerciseDetailVM(
        Long id,
        String title,
        String description,
        String deadline,
        List<ExerciseFileVM> files,
        ClassroomVM classroom
) {
    public static ExerciseDetailVM fromModel (Exercise exercise, List<ExerciseFileVM> files) {
        String deadline = exercise.getSubmission_deadline() != null ?
                DateTimeUtils.convertLocalDateTimeToString(exercise.getSubmission_deadline()) : "";
        ClassroomVM classroomVM = ClassroomVM.fromModel(exercise.getClassroom());
        return new ExerciseDetailVM(classroomVM.id(), exercise.getTitle(), exercise.getDescription(), deadline ,files, classroomVM);
    }
}
