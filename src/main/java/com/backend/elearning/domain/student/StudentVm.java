package com.backend.elearning.domain.student;


import com.backend.elearning.domain.user.ERole;
import com.backend.elearning.domain.user.User;

public record StudentVm(
         Long id,
         String email,
         String firstName,
         String lastName,
         String gender,
         boolean active,
         String photoURL,
         String dateOfBirth,
         String role
) {
    public static StudentVm fromModelStudent (Student student) {
        return new StudentVm(student.getId(),
                student.getEmail(),
                student.getFirstName(),
                student.getLastName(),
                student.getGender().name(),
                student.isActive(),
                student.getPhoto(),
                student.getDateOfBirth().toString(),
                ERole.ROLE_STUDENT.name());
    }
}
