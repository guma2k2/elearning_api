package com.backend.elearning.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query(value = """
            select l
            from Lecture l
            left join fetch l.section 
            where l.id = :id
            """)
    Optional<Lecture> findByIdSection(@Param("id") Long id);


    @Query(value = """
            select l
            from Lecture l
            left join fetch l.section s
            where s.id = :sectionId
            """)
    List<Lecture> findBySectionId(@Param("sectionId") Long sectionId);
}
