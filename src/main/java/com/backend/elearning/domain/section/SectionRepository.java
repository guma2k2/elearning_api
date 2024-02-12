package com.backend.elearning.domain.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query(value = """
            select s
            from Section s
            left join fetch s.lectures
            where s.id = :id
            """)
    Optional<Section> findByIdLecturesQuizzes(@Param("id") Long id);

    @Query(value = """
            select distinct s
            from Section s
            left join fetch s.quizzes
            where s = :section
            """)
    Optional<Section> findByIdQuizzes(@Param("section") Section section);
}
