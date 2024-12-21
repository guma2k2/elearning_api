package com.backend.elearning.domain.classroom;

import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;

public record ClassroomVM (
        Long id,
        String name,
        String description,
        String image,
        UserGetVM user
) {
    public static ClassroomVM fromModel(Classroom classroom) {
        UserGetVM userGetVM = UserGetVM.fromModel(classroom.getCourse().getUser());
        return new ClassroomVM(classroom.getId(), classroom.getName(), classroom.getDescription(), classroom.getImage(), userGetVM);
    }
}
