package com.backend.elearning.domain.excercise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/exercises")
    public ResponseEntity<ExerciseVM> create (
            @RequestBody ExercisePostVM referencePostVM
    ) {
        ExerciseVM referenceVM = exerciseService.create(referencePostVM);
        return ResponseEntity.ok().body(referenceVM);
    }

    @PutMapping("/exercises/{exerciseId}")
    public ResponseEntity<ExerciseVM> update (
            @RequestBody ExercisePostVM referencePostVM,
            @PathVariable("exerciseId") Long exerciseId
    ) {
        ExerciseVM referenceVM = exerciseService.update(referencePostVM, exerciseId);
        return ResponseEntity.ok().body(referenceVM);
    }

    @GetMapping("/exercises/{exerciseId}")
    public ResponseEntity<ExerciseDetailVM> get (
            @PathVariable("exerciseId") Long exerciseId
    ) {
        ExerciseDetailVM response = exerciseService.getById(exerciseId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/exercises/{exerciseId}")
    public ResponseEntity<String> delete (
          @PathVariable("exerciseId")  Long exerciseId
    ) {
        exerciseService.delete(exerciseId);
        return ResponseEntity.ok().body("deleted ");
    }
}
