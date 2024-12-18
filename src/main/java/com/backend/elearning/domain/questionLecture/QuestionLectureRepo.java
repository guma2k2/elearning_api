package com.backend.elearning.domain.questionLecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionLectureRepo extends JpaRepository<QuestionLecture, Long> {


    @Query("""
        select ql 
        from QuestionLecture ql 
        join ql.lecture l 
        join ql.student s 
        join l.section se
        where l.id = :lectureId
    """)
    List<QuestionLecture> getByLectureId(@Param("lectureId") Long lectureId);


    @Query("""
        select ql 
        from QuestionLecture ql 
        join ql.lecture l 
        join ql.student s 
        join l.section se
        join se.course c
        where c.id = :courseId
    """)
    List<QuestionLecture> getByCourse(@Param("courseId") Long courseId);


    @Query("""
        select ql 
        from QuestionLecture ql 
        join ql.lecture l 
        join ql.student s 
        join l.section se
        where se.id = :sectionId
    """)
    List<QuestionLecture> getBySection(@Param("sectionId") Long sectionId);
}
