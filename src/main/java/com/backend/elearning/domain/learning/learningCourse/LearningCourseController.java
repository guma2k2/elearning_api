package com.backend.elearning.domain.learning.learningCourse;

import com.backend.elearning.domain.lecture.LectureVm;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/learning-course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = LectureVm.class)))
    })
    public ResponseEntity<Void> createCourse (
            @RequestParam("courseId") Long courseId
    ) {
        learningCourseService.createLearningCourseForStudent(courseId);
        return ResponseEntity.noContent().build();
    }

}
