package com.backend.elearning.domain.questionLecture.studentAnswer;

import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.domain.questionLecture.AnswerLecture;
import com.backend.elearning.domain.questionLecture.QuestionLecturePostVM;
import com.backend.elearning.domain.questionLecture.QuestionLectureVM;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @Content(schema = @Schema(implementation = AnswerLecture.class)))
    })
    public ResponseEntity<AnswerLecture> create (
            @RequestBody StudentAnswerPostVM studentAnswerPostVM
    ) {
        AnswerLecture studentAnswerVM = studentAnswerService.create(studentAnswerPostVM);
        return ResponseEntity.status(HttpStatus.OK).body(studentAnswerVM);
    }

    @PutMapping("/student-answer/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created", content =
            @Content(schema = @Schema(implementation = AnswerLecture.class)))
    })
    public ResponseEntity<AnswerLecture> update (
            @RequestBody StudentAnswerPostVM studentAnswerPostVM,
            @PathVariable("id") Long id
    ) {
        AnswerLecture studentAnswerVM = studentAnswerService.update(studentAnswerPostVM, id);
        return ResponseEntity.status(HttpStatus.OK).body(studentAnswerVM);
    }

    @DeleteMapping("/student-answer/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id
    ) {
        studentAnswerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
