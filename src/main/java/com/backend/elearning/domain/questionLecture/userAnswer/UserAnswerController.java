package com.backend.elearning.domain.questionLecture.userAnswer;

import com.backend.elearning.domain.questionLecture.AnswerLecture;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswerPostVM;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserAnswerController {

    private final UserAnswerService userAnswerService;


    public UserAnswerController(UserAnswerService userAnswerService) {
        this.userAnswerService = userAnswerService;
    }

    @PostMapping("/user-answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created", content =
            @Content(schema = @Schema(implementation = AnswerLecture.class)))
    })
    public ResponseEntity<AnswerLecture> create (
            @RequestBody UserAnswerPostVM userAnswerPostVM
    ) {
        AnswerLecture userAnswerVM = userAnswerService.create(userAnswerPostVM);
        return ResponseEntity.status(HttpStatus.OK).body(userAnswerVM);
    }


    @PutMapping("/user-answer/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created", content =
            @Content(schema = @Schema(implementation = AnswerLecture.class)))
    })
    public ResponseEntity<AnswerLecture> update (
            @RequestBody UserAnswerPostVM userAnswerPostVM,
            @PathVariable("id") Long id
    ) {
        AnswerLecture studentAnswerVM = userAnswerService.update(userAnswerPostVM, id);
        return ResponseEntity.status(HttpStatus.OK).body(studentAnswerVM);
    }

    @DeleteMapping("/user-answer/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id
    ) {
        userAnswerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
