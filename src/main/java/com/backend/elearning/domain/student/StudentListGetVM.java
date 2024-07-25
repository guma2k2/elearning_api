package com.backend.elearning.domain.student;

import com.backend.elearning.domain.user.ERole;

public record StudentListGetVM (
        Long id,
        String email,
        String firstName,
        String lastName,
        String gender,
        boolean active,
        String photo,
        String dateOfBirth
){
    public static StudentListGetVM fromModelStudent (Student student) {
        String gender = student.getGender() != null ? student.getGender().name() : "";
        String dateOfBirth = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "";

        return new StudentListGetVM(student.getId(),
                student.getEmail(),
                student.getFirstName(),
                student.getLastName(),
                gender,
                student.isActive(),
                student.getPhoto(),
                dateOfBirth);
    }
}
