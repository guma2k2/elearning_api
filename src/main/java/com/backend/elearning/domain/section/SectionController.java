package com.backend.elearning.domain.section;

import com.backend.elearning.domain.course.CourseVM;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SectionController {

    private final SectionService sectionService;


    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/admin/sections")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = CourseVM.class)))
    })
    public ResponseEntity<SectionVM> createCourse (
            @RequestBody SectionPostVM sectionPostVM
    ) {
        SectionVM sectionVM = sectionService.create(sectionPostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionVM);
    }

    @GetMapping("/admin/sections/{id}")
    public ResponseEntity<SectionVM> getSectionById (
            @PathVariable("id") Long sectionId
    ) {
        SectionVM sectionVM = sectionService.getById(sectionId);
        return ResponseEntity.ok().body(sectionVM);
    }
}
