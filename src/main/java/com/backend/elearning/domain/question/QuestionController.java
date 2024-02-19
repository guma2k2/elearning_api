package com.backend.elearning.domain.question;

import com.backend.elearning.exception.ErrorVm;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/admin/questions/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successful",  content =
            @Content(schema = @Schema(implementation = QuestionVM.class))),
            @ApiResponse(responseCode = "404", description = "Not found",  content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<QuestionVM> updateQuestion (
            @RequestBody QuestionPostVM questionPostVM,
            @PathVariable("id") Long questionId
    ) {
        QuestionVM questionVM = questionService.update(questionPostVM, questionId);
        return ResponseEntity.ok().body(questionVM);
    }

    @GetMapping("/admin/questions/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get successful",  content =
            @Content(schema = @Schema(implementation = QuestionVM.class))),
            @ApiResponse(responseCode = "404", description = "Not found",  content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<QuestionVM> getQuestionById (
            @PathVariable("id") Long questionId
    ) {
        QuestionVM questionVM = questionService.getById(questionId);
        return ResponseEntity.ok().body(questionVM);
    }
}
