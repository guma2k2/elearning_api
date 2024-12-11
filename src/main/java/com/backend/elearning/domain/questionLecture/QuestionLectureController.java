package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.domain.note.NotePostVM;
import com.backend.elearning.domain.note.NoteVM;
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
public class QuestionLectureController {

    private final QuestionLectureService questionLectureService;


    public QuestionLectureController(QuestionLectureService questionLectureService) {
        this.questionLectureService = questionLectureService;
    }
    @PostMapping("/question-lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created", content =
            @Content(schema = @Schema(implementation = LectureVm.class)))
    })
    public ResponseEntity<QuestionLectureVM> create (
            @RequestBody QuestionLecturePostVM questionLecturePostVM
    ) {
        QuestionLectureVM questionLectureVM = questionLectureService.create(questionLecturePostVM);
        return ResponseEntity.status(HttpStatus.OK).body(questionLectureVM);
    }


}
