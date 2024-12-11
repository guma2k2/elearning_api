package com.backend.elearning.domain.questionLecture.studentAnswer;

import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.domain.questionLecture.QuestionLecturePostVM;
import com.backend.elearning.domain.questionLecture.QuestionLectureVM;
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
public class StudentAnswerController {

    private final StudentAnswerService studentAnswerService;

    public StudentAnswerController(StudentAnswerService studentAnswerService) {
        this.studentAnswerService = studentAnswerService;
    }


    @PostMapping("/student-answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created", content =
            @Content(schema = @Schema(implementation = StudentAnswerVM.class)))
    })
    public ResponseEntity<StudentAnswerVM> create (
            @RequestBody StudentAnswerPostVM studentAnswerPostVM
    ) {
        StudentAnswerVM studentAnswerVM = studentAnswerService.create(studentAnswerPostVM);
        return ResponseEntity.status(HttpStatus.OK).body(studentAnswerVM);
    }
}
