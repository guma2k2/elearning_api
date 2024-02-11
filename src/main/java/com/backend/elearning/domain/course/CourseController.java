package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.CategoryPostVM;
import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.security.AuthUserDetails;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/admin/courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = CourseVM.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated title", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<CourseVM> createCourse (
            @RequestBody CoursePostVM coursePostVM,
            @AuthenticationPrincipal AuthUserDetails authUserDetails
            ) {
        Long userId = authUserDetails.getId();
        CourseVM courseVM = courseService.create(coursePostVM, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseVM);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseVM> getCourseById (
            @PathVariable("id") Long courseId
    ) {
        CourseVM courseVM = courseService.getCourseById(courseId);
        return ResponseEntity.ok().body(courseVM);
    }
}
