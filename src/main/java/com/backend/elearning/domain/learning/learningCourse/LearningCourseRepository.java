package com.backend.elearning.domain.learning.learningCourse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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


    @Query("""
        select count (lc.id)
        from LearningCourse lc 
        join lc.course c
        join lc.student
        where c.id = :courseId
    """)
    Long countStudentByCourseId (@Param("courseId") Long courseId);


    @Query("""
        select lc 
        from LearningCourse lc
        join lc.course c 
        join lc.student s
        where c.id = :courseId and s.email = :email
    """)
    Optional<LearningCourse> findByStudentAndCourse(@Param("email") String email, @Param("courseId") Long courseId);




    @Query("""
        select count(1) 
        from LearningCourse lc 
        join lc.course c 
        join c.user u
        where u.email = :email
    """)
    long countStudentByInstructorEmail(@Param("email") String email);

    @Query("""
        select lc 
        from LearningCourse lc 
        join lc.course c
        where c.id = :courseId
    """)
    List<LearningCourse> findByCourseId(@Param("courseId") Long courseId);
}
