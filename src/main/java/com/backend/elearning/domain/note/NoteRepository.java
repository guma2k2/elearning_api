package com.backend.elearning.domain.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {


    @Query("""
        select n
        from Note n 
        join fetch n.lecture l 
        join fetch l.section s 
        where s.id = :sectionId
    """)
    List<Note> findBySection(@Param("sectionId") Long sectionId);

    @Query("""
        select n
        from Note n 
        join fetch n.lecture l 
        join fetch l.section s 
        join fetch s.course c
        where c.id = :courseId
    """)
    List<Note> findByCourse(@Param("courseId") Long courseId);

    @Query("""
        select n
        from Note n 
        join fetch n.lecture l 
        where n.id = :noteId
    """)
    Optional<Note> findByIdCustom(@Param("noteId") Long noteId);

}
