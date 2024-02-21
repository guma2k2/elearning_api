package com.backend.elearning.domain.lecture;

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
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping("/admin/lectures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = LectureVm.class)))
    })
    public ResponseEntity<LectureVm> createCourse (
            @RequestBody LecturePostVM lecturePostVM
    ) {
        LectureVm lectureVm = lectureService.create(lecturePostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureVm);
    }

    @PutMapping("/admin/lectures/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Section not same", content =
            @Content(schema = @Schema(implementation = ErrorVm.class)))
    })
    public ResponseEntity<LectureVm> update (
            @RequestBody LecturePostVM lecturePutVM,
            @PathVariable("id") Long lectureId
    ) {
        LectureVm lectureVm = lectureService.update(lecturePutVM, lectureId);
        return ResponseEntity.ok().body(lectureVm);
    }

    @DeleteMapping("/admin/lectures/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete success"),
    })
    public ResponseEntity<Void> deleteLectureById (
            @PathVariable("id") Long lectureId
    ) {
        lectureService.delete(lectureId);
        return ResponseEntity.noContent().build();
    }
}
