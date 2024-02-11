package com.backend.elearning.domain.quiz;

import com.backend.elearning.domain.lecture.LecturePostVM;
import com.backend.elearning.domain.lecture.LectureService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/admin/quizzes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content")
    })
    public ResponseEntity<Void> createCourse (
            @RequestBody QuizPostVM quizPostVM
    ) {
        quizService.create(quizPostVM);
        return ResponseEntity.noContent().build();
    }
}

