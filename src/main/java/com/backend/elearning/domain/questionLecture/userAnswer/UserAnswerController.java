package com.backend.elearning.domain.questionLecture.userAnswer;

import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswerPostVM;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswerVM;
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
public class UserAnswerController {

    private final UserAnswerService userAnswerService;


    public UserAnswerController(UserAnswerService userAnswerService) {
        this.userAnswerService = userAnswerService;
    }

    @PostMapping("/user-answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created", content =
            @Content(schema = @Schema(implementation = UserAnswerVM.class)))
    })
    public ResponseEntity<UserAnswerVM> create (
            @RequestBody UserAnswerPostVM userAnswerPostVM
    ) {
        UserAnswerVM userAnswerVM = userAnswerService.create(userAnswerPostVM);
        return ResponseEntity.status(HttpStatus.OK).body(userAnswerVM);
    }
}