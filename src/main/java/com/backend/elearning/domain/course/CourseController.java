package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.security.AuthUserDetails;
import com.backend.elearning.utils.Constants;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping("/admin/courses/paging")
    public ResponseEntity<PageableData<CourseVM>> getPageableCourse (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        PageableData<CourseVM> pageableCourses = courseService.getPageableCourses(pageNum, pageSize);
        return ResponseEntity.ok().body(pageableCourses);
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
//        Long userId = authUserDetails.getId();
        Long userId = 1L;
        CourseVM courseVM = courseService.create(coursePostVM, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseVM);
    }

    @PutMapping("/admin/courses/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted", content =
            @Content(schema = @Schema(implementation = CourseVM.class))),
            @ApiResponse(responseCode = "404", description = "Bad Request", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated title", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<CourseVM> updateCourse (
            @RequestBody CoursePostVM coursePostVM,
            @PathVariable("id") Long courseId,
            @AuthenticationPrincipal AuthUserDetails authUserDetails
    ) {
//        Long userId = authUserDetails.getId();
        Long userId = 1L;
        CourseVM courseVM = courseService.update(coursePostVM, userId, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(courseVM);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseVM> getCourseById (
            @PathVariable("id") Long courseId
    ) {
        CourseVM courseVM = courseService.getCourseById(courseId);
        return ResponseEntity.ok().body(courseVM);
    }
}
