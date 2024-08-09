package com.backend.elearning.domain.course;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.utils.Constants;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PageableData<CourseVM> pageableCourses = courseService.getPageableCourses(pageNum, pageSize, keyword);
        return ResponseEntity.ok().body(pageableCourses);
    }

    @GetMapping("/courses/category/{categoryId}")
    public ResponseEntity<List<CourseListGetVM>> getPageableCourse (
            @PathVariable("categoryId") Integer categoryId
    ) {
        List<CourseListGetVM> courseListGetVMS = courseService.getCoursesByCategoryId(categoryId);
        return ResponseEntity.ok().body(courseListGetVMS);
    }


    @GetMapping("/courses/search")
    public ResponseEntity<PageableData<CourseListGetVM>> getCoursesByMultiQuery (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "ratingStar", required = false) Float rating,
            @RequestParam(value = "level", required = false) String[] level,
            @RequestParam(value = "free", required = false) Boolean[] free,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "topicId", required = false) Integer topicId
            ) {
        PageableData<CourseListGetVM> pageableCourses = courseService.getCoursesByMultiQuery(pageNum, pageSize, keyword, rating, level, free, categoryName, topicId);
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
            @RequestBody CoursePostVM coursePostVM
    ) {
        CourseVM courseVM = courseService.create(coursePostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseVM);
    }

    @PutMapping("/admin/courses/{id}/status/{status}")
    public ResponseEntity<Void> updateReview(@PathVariable("status") boolean status, @PathVariable("id") Long courseId){
        courseService.updateStatusCourse(status, courseId);
        return ResponseEntity.noContent().build();
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
            @PathVariable("id") Long courseId
    ) {
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

    @GetMapping("/courses/{slug}/learn")
    public ResponseEntity<CourseLearningVm> getCourseBySlug (
            @PathVariable("slug") String slug
    ) {
        CourseLearningVm course = courseService.getCourseBySlug(slug);
        return ResponseEntity.ok().body(course);
    }
    @DeleteMapping("/admin/courses/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id
    ) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
