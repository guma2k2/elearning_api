package com.backend.elearning.domain.learning.learningLecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningLectureRepository extends JpaRepository<LearningLecture, Long> {


    @Query("""
           select ll
           from LearningLecture ll
           left join fetch ll.student s
           left join fetch ll.lecture l
           left join fetch l.section se
           left join fetch se.course c
           where ll.accessTime = (
               select max(ll2.accessTime)
               from LearningLecture ll2
               where ll2.student.id = s.id
           )
           and s.email = :email
           and c.slug = :slug
            """)
    Optional<LearningLecture> findMaxAccessTimeByEmailAndCourseSlug(@Param("email") String email, @Param("slug") String slug);



    @Query("""
        select count(ll)
        from LearningLecture  ll 
        join  ll.student st
        join  ll.lecture l 
        join  l.section s 
        join  s.course c
        where st.email = :email and c.id = :courseId
    """)
    Long countByCourseAndStudent(@Param("email") String email, @Param("courseId") Long courseId);


    @Query("""
        select ll 
        from LearningLecture ll 
        left join fetch ll.student s 
        left join fetch  ll.lecture l 
        where s.email = :email and l.id = :lectureId
    """)
    Optional<LearningLecture> findByEmailAndLectureId(@Param("email") String email, @Param("lectureId") Long lectureId);


}
