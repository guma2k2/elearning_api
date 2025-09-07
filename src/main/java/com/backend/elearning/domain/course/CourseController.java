package com.backend.elearning.domain.course;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.utils.Constants;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/courses/spec")
    public ResponseEntity<PageableData<CourseVM>> testSpecification (
            Pageable pageable,
            @RequestParam(value = "course", required = false) String[] course,
            @RequestParam(value = "category", required = false) String [] category

    ) {
        PageableData<CourseVM> pageableCourses = courseService.advanceSearchWithSpecifications(pageable, course, category);
        return ResponseEntity.ok().body(pageableCourses);
    }



    @GetMapping("/admin/courses/paging")
    public ResponseEntity<PageableData<CourseVM>> getPageableCourse (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) CourseStatus status

    ) {
        PageableData<CourseVM> pageableCourses = courseService.getPageableCourses(pageNum, pageSize, keyword, status);
        return ResponseEntity.ok().body(pageableCourses);
    }

    @GetMapping("/courses/category/{categoryName}")
    public ResponseEntity<List<CourseListGetVM>> getPageableCourse (
            @PathVariable("categoryName") String categoryName,
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
    @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        List<CourseListGetVM> courseListGetVMS = courseService.getCoursesByCategory(categoryName, pageNum, pageSize);
        return ResponseEntity.ok().body(courseListGetVMS);
    }


    @GetMapping("/courses/search")
    public ResponseEntity<PageableData<CourseListGetVM>> getCoursesByMultiQueryWithPageable (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_COURSE_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "ratingStar", required = false) Float rating,
            @RequestParam(value = "level", required = false) List<String> level,
            @RequestParam(value = "free", required = false) List<Boolean> free,
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

    @PutMapping("/admin/courses/{id}/status")
    public ResponseEntity<Void> updateReview(@RequestBody CourseStatusPostVM courseStatusPostVM, @PathVariable("id") Long courseId){
        courseService.updateStatusCourse(courseStatusPostVM, courseId);
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
    // this
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

    @GetMapping("/admin/courses/promotions/{promotionId}")
    public ResponseEntity<List<CourseAssignPromotion>> getByPromotionId(@PathVariable("promotionId") Long promotionId){
        List<CourseAssignPromotion> courses = courseService.getByPromotionId(promotionId);
        return ResponseEntity.ok().body(courses);
    }

    @GetMapping("/courses/suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok().body(courseService.getSuggestion(keyword));
    }


}
