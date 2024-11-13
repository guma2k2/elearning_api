package com.backend.elearning.domain.classroom;

import java.util.List;

public interface ClassroomService {

    ClassroomVM create(ClassroomPostVM classroomPostVM);
    List<ClassroomVM> getByCourseId(Long courseId);

    ClassroomGetVM getById(Long id);
}
