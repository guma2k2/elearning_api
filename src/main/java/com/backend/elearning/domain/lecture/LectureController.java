package com.backend.elearning.domain.lecture;

import com.backend.elearning.domain.course.CourseVM;
import com.backend.elearning.domain.section.SectionPostVM;
import com.backend.elearning.domain.section.SectionVM;
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
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping("/admin/lectures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content")
    })
    public ResponseEntity<Void> createCourse (
            @RequestBody LecturePostVM lecturePostVM
    ) {
        lectureService.create(lecturePostVM);
        return ResponseEntity.noContent().build();
    }
}