package com.backend.elearning.domain.classroom;

import com.backend.elearning.domain.common.Event;
import com.backend.elearning.domain.user.UserGetVM;

import java.util.List;

public record ClassroomGetVM (
        Long id,
        String name,
        String description,
        String image,
        List<Event> events,
        UserGetVM user
) {
    public static ClassroomGetVM fromModel(Classroom classroom, List<Event> events) {
        UserGetVM userGetVM = UserGetVM.fromModel(classroom.getCourse().getUser());
        return new ClassroomGetVM(classroom.getId(),
                classroom.getName(), classroom.getDescription(), classroom.getImage(), events, userGetVM);
    }
}
