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
    public static UserVm fromModel (User user, String photoUrl) {
        return new UserVm(user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender().name(),
                user.isActive(),
                photoUrl,
                user.getDateOfBirth().toString(),
                user.getRole().name());
    }

    public static UserVm fromModelStudent (Student student) {
        return new UserVm(student.getId(),
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
