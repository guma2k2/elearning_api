package com.backend.elearning.domain.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = """
            select q
            from Question q
            left join fetch q.quiz quiz
            left join fetch q.answers a
            where quiz.id = :quizId
            """)
    List<Question> findByQuiz(@Param("quizId") Long quizId);
    @Query(value = """
            select q
            from Question q
            left join fetch q.quiz quiz
            left join fetch q.answers a
            where q.id = :id
            """)
    Optional<Question> findByIdReturnAnswers(@Param("id") Long id);

}
