package com.backend.elearning.domain.learning.learningQuiz;

import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningQuizRepository extends JpaRepository<LearningQuiz, Long> {


    @Query("""
           select ll
           from LearningQuiz ll
           left join fetch ll.student s
           left join fetch ll.quiz l
           left join fetch l.section se
           left join fetch se.course c
           where ll.accessTime = (
               select max(ll2.accessTime)
               from LearningQuiz ll2
               where ll2.student.id = s.id
           )
           and s.email = :email
           and c.slug = :slug
                                                                                               
     """)
    Optional<LearningQuiz> findMaxAccessTimeByEmailAndCourseSlug(@Param("email") String email, @Param("slug") String slug);

    @Query("""
        select count(ll)
        from LearningQuiz ll
        join ll.student st
        join ll.quiz l 
        join l.section s 
        join s.course c
        where st.email = :email and c.id = :courseId
    """)
    Long countByCourseAndStudent(@Param("email") String email, @Param("courseId") Long courseId);


    @Query("""
        select ll 
        from LearningQuiz ll 
        left join fetch ll.student s 
        left join fetch  ll.quiz l 
        where s.email = :email and l.id = :quizId
    """)
    Optional<LearningQuiz> findByEmailAndQuizId(@Param("email") String email, @Param("quizId") Long quizId);

}
