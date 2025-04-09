package com.backend.elearning.domain.studentExercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExerciseRepository extends JpaRepository<StudentExercise, Long> {

    @Query("""
        select s
        from StudentExercise s 
        join fetch s.student st 
        join fetch s.exercise e 
        where st.email = :email and e.id = :exerciseId
    """)
    Optional<StudentExercise> findByExerciseIdAndStudent(@Param("email") String email,
                                                         @Param("exerciseId") Long exerciseId);


    @Query("""
        select s
        from StudentExercise s 
        join fetch s.student st 
        join fetch s.exercise e 
        where e.id = :exerciseId and s.submitted = true 
    """)
    List<StudentExercise> findByExerciseId(@Param("exerciseId") Long exerciseId);
}
