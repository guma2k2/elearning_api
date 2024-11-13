package com.backend.elearning.domain.classroom;

public record ClassroomVM (
        Long id,
        String name,
        String description,
        String image
) {
    public static ClassroomVM fromModel(Classroom classroom) {
        return new ClassroomVM(classroom.getId(), classroom.getName(), classroom.getDescription(), classroom.getImage());
    }
}
