package com.backend.elearning.domain.exerciseFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseFileRepo extends JpaRepository<ExerciseFile, Long> {


    @Query("""
            select r 
            from ExerciseFile r
            join r.exercise re
            where re.id = :exerciseId
            """)
    List<ExerciseFile> findByExercise(@Param("exerciseId") Long exerciseId);
}
