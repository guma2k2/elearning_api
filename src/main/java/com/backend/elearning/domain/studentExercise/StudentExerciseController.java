package com.backend.elearning.domain.studentExercise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentExerciseController {

    private final StudentExerciseService studentExerciseService;

    public StudentExerciseController(StudentExerciseService studentExerciseService) {
        this.studentExerciseService = studentExerciseService;
    }


    @PostMapping("/student-exercises")
    public ResponseEntity<StudentExerciseVM> create (
            @RequestBody StudentExercisePostVM studentExercisePostVM
    ) {
        StudentExerciseVM studentExerciseVM = studentExerciseService.create(studentExercisePostVM);
        return ResponseEntity.ok().body(studentExerciseVM);
    }

    @PutMapping("/student-exercises/{id}")
    public ResponseEntity<StudentExerciseVM> update (
            @RequestBody StudentExercisePostVM studentExercisePostVM,
            @PathVariable("id") Long id
    ) {
        StudentExerciseVM studentExerciseVM = studentExerciseService.update(studentExercisePostVM, id);
        return ResponseEntity.ok().body(studentExerciseVM);
    }

    @DeleteMapping("/student-exercises/{id}")
    public ResponseEntity<String> delete (
          @PathVariable("id")  Long id
    ) {
        studentExerciseService.delete(id);
        return ResponseEntity.ok().body("deleted ");
    }

    @GetMapping("/student-exercises/exercise/{exerciseId}")
    public ResponseEntity<StudentExerciseVM> getByExerciseAndStudent (
            @PathVariable("exerciseId")  Long exerciseId
    ) {
        StudentExerciseVM studentExerciseVM = studentExerciseService.getByExerciseId(exerciseId);
        if (studentExerciseVM == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(studentExerciseVM);
    }

    @GetMapping("/student-exercises/exercise/{exerciseId}/list")
    public ResponseEntity<List<StudentExerciseGetVM>> getByExrcise (
            @PathVariable("exerciseId")  Long exerciseId
    ) {
        List<StudentExerciseGetVM> studentExerciseVMS = studentExerciseService.getListByExerciseId(exerciseId);
        return ResponseEntity.ok().body(studentExerciseVMS);
    }
}
