package com.backend.elearning.domain.exerciseFile;

public record ExerciseFileVM(
        Long id,
        String fileName,
        String fileUrl

) {

    public static ExerciseFileVM fromModel(ExerciseFile exerciseFile) {

        return new ExerciseFileVM(exerciseFile.getId(), exerciseFile.getFileName(), exerciseFile.getFileUrl()
                );
    }
}
