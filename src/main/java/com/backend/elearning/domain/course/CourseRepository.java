package com.backend.elearning.domain.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = """
            select count(1)
            from Course c
            where c.title = :title and (c.id != :id or :id != null)
            """)
    Long countExistByTitle(@Param("title") String title,
                           @Param("id") Long id);

    @Query(value = """
            select c
            from Course c
            left join fetch c.sections s
            where s.id = :id
            """)
    Optional<Course> findByIdReturnSections(@Param("id") Long courseId);
}
