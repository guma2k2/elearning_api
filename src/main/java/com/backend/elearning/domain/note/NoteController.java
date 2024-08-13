package com.backend.elearning.domain.note;

import com.backend.elearning.domain.course.CourseVM;
import com.backend.elearning.domain.lecture.LecturePostVM;
import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.exception.ErrorVm;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes/section/{sectionId}")
    public ResponseEntity<List<NoteVM>> getBySection (
            @PathVariable("sectionId") Long sectionId
    ) {
        List<NoteVM> noteVMS = noteService.getBySectionStudent(sectionId);
        return ResponseEntity.ok().body(noteVMS);
    }

    @GetMapping("/notes/course/{courseId}")
    public ResponseEntity<List<NoteVM>> getByCourse (
            @PathVariable("courseId") Long courseId
    ) {
        List<NoteVM> noteVMS = noteService.getAllByCourse(courseId);
        return ResponseEntity.ok().body(noteVMS);
    }

    @PostMapping("/notes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = LectureVm.class)))
    })
    public ResponseEntity<NoteVM> createNote (
            @RequestBody NotePostVM notePostVM
    ) {
        NoteVM noteVM = noteService.create(notePostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteVM);
    }

    @PutMapping("/notes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Section not same", content =
            @Content(schema = @Schema(implementation = ErrorVm.class)))
    })
    public ResponseEntity<NoteVM> update (
            @RequestBody NotePostVM notePostVM
    ) {
        NoteVM noteVM = noteService.update(notePostVM);
        return ResponseEntity.ok().body(noteVM);
    }

    @DeleteMapping("/notes/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete success"),
    })
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long noteId
    ) {
        noteService.delete(noteId);
        return ResponseEntity.noContent().build();
    }
}
