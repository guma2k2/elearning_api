package com.backend.elearning.domain.questionLecture.studentAnswer;

import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepo extends JpaRepository<StudentAnswer, Long> {

    @Query("""
        select ua 
        from StudentAnswer ua 
        join ua.questionLecture q 
        where q.id = :questionLectureId
    """)
    List<StudentAnswer> getByQuestionLectureId(@Param("questionLectureId") Long questionLectureId);
}
