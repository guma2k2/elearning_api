package com.backend.elearning.domain.exerciseFile;

import com.backend.elearning.domain.referencefile.ReferenceFileVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ExerciseFileController {

    private final ExerciseFileService exerciseFileService;

    public ExerciseFileController(ExerciseFileService exerciseFileService) {
        this.exerciseFileService = exerciseFileService;
    }

    @PostMapping("/exercise-files")
    public ResponseEntity<ExerciseFileVM> create (
            @RequestBody ExerciseFilePostVM exerciseFilePostVM
    ) {
        ExerciseFileVM exerciseFileVM = exerciseFileService.create(exerciseFilePostVM);
        return ResponseEntity.ok().body(exerciseFileVM);
    }

    @PutMapping("/exercise-files/{id}")
    public ResponseEntity<ExerciseFileVM> update (
            @RequestBody ExerciseFilePostVM exerciseFilePostVM,
            @PathVariable("id") Long id
    ) {
        ExerciseFileVM exerciseFileVM = exerciseFileService.update(exerciseFilePostVM, id);
        return ResponseEntity.ok().body(exerciseFileVM);
    }


    @DeleteMapping("/exercise-files/{id}")
    public ResponseEntity<String> delete (
            @PathVariable("id") Long id
    ) {
        exerciseFileService.delete(id);
        return ResponseEntity.ok().body("deleted");
    }
}
