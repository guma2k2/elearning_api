package com.backend.elearning.domain.learning.learningCourse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LearningCourseController {
    private final LearningCourseService learningCourseService;


    public LearningCourseController(LearningCourseService learningCourseService) {
        this.learningCourseService = learningCourseService;
    }

    @GetMapping("/learning-course/student")
    public ResponseEntity<List<LearningCourseVM>> getLearningCoursesByStudent() {
        List<LearningCourseVM> learningCoursesByStudent = learningCourseService.getByStudent();
        return ResponseEntity.ok().body(learningCoursesByStudent);
    }

}
