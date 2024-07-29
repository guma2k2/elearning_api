package com.backend.elearning.domain.learning.learningQuiz;

import com.backend.elearning.domain.learning.learningLecture.LearningLecturePostVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LearningQuizController {
    private final LearningQuizService learningQuizService;

    public LearningQuizController(LearningQuizService learningQuizService) {
        this.learningQuizService = learningQuizService;
    }


    @PostMapping("/learning-quizzes")
    public ResponseEntity<Void> create(@RequestBody LearningQuizPostVM learningQuizPostVM) {
        learningQuizService.create(learningQuizPostVM);
        return ResponseEntity.noContent().build();
    }

}
