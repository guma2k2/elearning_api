package com.backend.elearning.domain.user;

import com.backend.elearning.domain.student.Student;

public record UserGetVM(
        Long id,
        String firstName,
        String lastName,
        String email,
        String photo,
        String role
) {
    public static UserGetVM fromModel(User user) {
        return new UserGetVM(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoto(), user.getRole().name());
    }

    public static UserGetVM fromModelStudent(Student student) {
        return new UserGetVM(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getPhoto(), ERole.ROLE_STUDENT.name());
    }
}
