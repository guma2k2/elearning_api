package com.backend.elearning.domain.question;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    @PostMapping("/admin/questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content")
    })
    public ResponseEntity<Void> createQuestion (
            @RequestBody QuestionPostVM questionPostVM
    ) {
        questionService.create(questionPostVM);
        return ResponseEntity.noContent().build();
    }
}
