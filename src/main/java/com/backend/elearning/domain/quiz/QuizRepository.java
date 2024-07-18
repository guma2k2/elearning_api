package com.backend.elearning.domain.quiz;

import com.backend.elearning.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query(value = """
            select q
            from Quiz q
            left join fetch q.section
            where q.id = :id
            """)
    Optional<Quiz> findByIdReturnSection(@Param("id") Long id);

    @Query(value = """
            select q
            from Quiz q
            left join fetch q.questions
            where q.id = :id
            """)
    Optional<Quiz> findByIdReturnQuestions(@Param("id") Long id);


    @Query(value = """
            select l
            from Quiz l
            left join fetch l.section s
            where s.id = :sectionId
            """)
    List<Quiz> findBySectionId(@Param("sectionId") Long sectionId);
}
