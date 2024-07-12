package com.backend.elearning.domain.user;


import com.backend.elearning.domain.student.Student;

public record UserVm (
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
    public static UserVm fromModel (User user) {
        return new UserVm(user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender().name(),
                user.isActive(),
                user.getPhoto(),
                user.getDateOfBirth().toString(),
                user.getRole().name());
    }

    public static UserVm fromModelStudent (Student student) {
        String gender = student.getGender() != null ? student.getGender().name() : "";
        String dateOfBirth = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "";
        return new UserVm(student.getId(),
                student.getEmail(),
                student.getFirstName(),
                student.getLastName(),
                gender,
                student.isActive(),
                student.getPhoto(),
                dateOfBirth,
                ERole.ROLE_STUDENT.name());
    }
}
