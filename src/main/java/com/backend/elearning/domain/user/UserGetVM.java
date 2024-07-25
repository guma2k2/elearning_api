package com.backend.elearning.domain.user;

import com.backend.elearning.domain.student.Student;

public record UserGetVM(
        Long id,
        String firstName,
        String lastName,
        String email,
        String photo
) {
    public static UserGetVM fromModel(User user) {
        return new UserGetVM(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoto());
    }

    public static UserGetVM fromModelStudent(Student student) {
        return new UserGetVM(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getPhoto());
    }
}
