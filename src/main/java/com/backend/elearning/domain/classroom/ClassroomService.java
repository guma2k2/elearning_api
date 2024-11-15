package com.backend.elearning.domain.classroom;

import java.util.List;

public interface ClassroomService {

    ClassroomVM create(ClassroomPostVM classroomPostVM);

    ClassroomVM update(ClassroomPostVM classroomPostVM, Long classroomId);
    List<ClassroomVM> getByCourseId(Long courseId);

    ClassroomGetVM getById(Long id);
}
