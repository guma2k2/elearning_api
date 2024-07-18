package com.backend.elearning.domain.learning.learningCourse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningCourseRepository extends JpaRepository<LearningCourse, Long> {


    @Query("""  
        select lc 
        from LearningCourse lc 
        join fetch lc.course c
        join fetch lc.student s
        left join fetch c.sections 
        where s.email  = :email
    """)
    List<LearningCourse> findByStudentEmail(@Param("email") String email);
}
