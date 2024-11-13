package com.backend.elearning.domain.classroom;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseVM;
import com.backend.elearning.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping("/classrooms")
    public ResponseEntity<ClassroomVM> create (
            @RequestBody ClassroomPostVM classroomPostVM
    ) {
        ClassroomVM classroomVM = classroomService.create(classroomPostVM);
        return ResponseEntity.ok().body(classroomVM);
    }

    @GetMapping("/classrooms/course/{courseId}")
    public ResponseEntity<List<ClassroomVM>> getByCourseId (
            @PathVariable("courseId") Long courseId
    ) {
        List<ClassroomVM> classroomVMS = classroomService.getByCourseId(courseId);
        return ResponseEntity.ok().body(classroomVMS);
    }


    @GetMapping("/classrooms/{classroomId}")
    public ResponseEntity<ClassroomGetVM> getById (
            @PathVariable("classroomId") Long classroomId
    ) {
        ClassroomGetVM classroomGetVM = classroomService.getById(classroomId);
        return ResponseEntity.ok().body(classroomGetVM);
    }
}
