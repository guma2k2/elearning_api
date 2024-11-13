package com.backend.elearning.domain.classroom;

import com.backend.elearning.domain.common.Event;

import java.util.List;

public record ClassroomGetVM (
        Long id,
        String name,
        String description,
        String image,
        List<Event> events
) {
    public static ClassroomGetVM fromModel(Classroom classroom, List<Event> events) {
        return new ClassroomGetVM(classroom.getId(),
                classroom.getName(), classroom.getDescription(), classroom.getImage(), events);
    }
}
