package com.backend.elearning.domain.quiz;

import com.backend.elearning.domain.course.CourseVM;
import com.backend.elearning.domain.lecture.LecturePostVM;
import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.domain.section.SectionVM;
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
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/admin/quizzes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = QuizVM.class)))
    })
    public ResponseEntity<QuizVM> createCourse (
            @RequestBody QuizPostVM quizPostVM
    ) {
        QuizVM quizVM = quizService.create(quizPostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(quizVM);
    }
    @PutMapping("/admin/quizzes/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content =
            @Content(schema = @Schema(implementation = QuizVM.class))),
            @ApiResponse(responseCode = "400", description = "Section not same", content =
            @Content(schema = @Schema(implementation = ErrorVm.class)))
    })
    public ResponseEntity<QuizVM> update (
            @RequestBody QuizPostVM quizPostVM,
            @PathVariable("id") Long quizId
    ) {
        QuizVM quizVM = quizService.update(quizPostVM, quizId);
        return ResponseEntity.ok().body(quizVM);
    }

    @DeleteMapping("/admin/quizzes/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete success"),
            @ApiResponse(responseCode = "400", description = "Cannot delete quiz because it had quizzes", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "404", description = "Quiz not found", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<Void> deleteQuizById (
            @PathVariable("id") Long quizId
    ) {
        quizService.delete(quizId);
        return ResponseEntity.noContent().build();
    }
}

