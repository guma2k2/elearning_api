package com.backend.elearning.domain.question;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
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
            @ApiResponse(responseCode = "201", description = "Created",  content =
            @Content(schema = @Schema(implementation = QuestionVM.class)))
    })
    public ResponseEntity<QuestionVM> createQuestion (
            @RequestBody QuestionPostVM questionPostVM
    ) {
        QuestionVM questionVM = questionService.create(questionPostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionVM);
    }
}
