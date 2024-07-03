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
            left join fetch l.course c
            group by ll.student, ll.lecture
            having ll.accessTime = max(ll.accessTime) and s.email = :email and c.slug = :slug
            """)
    Optional<LearningLecture> findMaxAccessTimeByEmailAndCourseSlug(@Param("email") String email, @Param("slug") String slug);
}
