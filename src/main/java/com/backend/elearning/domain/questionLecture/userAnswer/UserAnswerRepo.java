package com.backend.elearning.domain.questionLecture.userAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepo extends JpaRepository<UserAnswer, Long> {


    @Query("""
        select ua 
        from UserAnswer ua 
        join ua.questionLecture q 
        where q.id = :questionLectureId
""")
    List<UserAnswer> getByQuestionLectureId(@Param("questionLectureId") Long questionLectureId);
}
