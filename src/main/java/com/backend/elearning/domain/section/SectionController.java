package com.backend.elearning.domain.section;

import com.backend.elearning.domain.course.CourseVM;
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
        SectionVM sectionVM = sectionService.getById(sectionId, null);
        return ResponseEntity.ok().body(sectionVM);
    }

    @PutMapping("/admin/sections/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update success", content =
            @Content(schema = @Schema(implementation = CourseVM.class))),
            @ApiResponse(responseCode = "400", description = "Course of section is not valid", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "404", description = "Section not found", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<SectionVM> updateCourse (
            @RequestBody SectionPostVM sectionPostVM,
            @PathVariable("id") Long id
    ) {
        SectionVM sectionVM = sectionService.update(sectionPostVM, id);
        return ResponseEntity.ok().body(sectionVM);
    }
    @DeleteMapping("/admin/sections/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete success"),
            @ApiResponse(responseCode = "400", description = "Cannot delete section because it had curriculums", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "404", description = "Section not found", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<Void> deleteSectionById (
            @PathVariable("id") Long sectionId
    ) {
        sectionService.delete(sectionId);
        return ResponseEntity.noContent().build();
    }
}
