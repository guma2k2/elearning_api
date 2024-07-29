package com.backend.elearning.domain.learning.learningLecture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LearningLectureController {

    private final LearningLectureService learningLectureService;

    public LearningLectureController(LearningLectureService learningLectureService) {
        this.learningLectureService = learningLectureService;
    }


    @PostMapping("/learning-lectures")
    public ResponseEntity<Void> create(@RequestBody LearningLecturePostVM learningLecturePostVM) {
        learningLectureService.create(learningLecturePostVM);
        return ResponseEntity.noContent().build();
    }


}
