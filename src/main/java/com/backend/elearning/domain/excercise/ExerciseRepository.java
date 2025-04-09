package com.backend.elearning.domain.excercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("""
        select e
        from Exercise e 
        join fetch e.classroom c
        join fetch c.course co 
        join fetch co.user u
    """)
    Optional<Exercise> findByIdCustom(@Param("id") Long id) ;
}
