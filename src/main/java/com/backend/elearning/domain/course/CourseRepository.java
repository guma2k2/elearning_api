package com.backend.elearning.domain.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            where c.id = :id
            """)
    Optional<Course> findByIdReturnSections(@Param("id") Long courseId);


    @Query(value = """
            select c
            from Course c
            join fetch c.category cat
            left join fetch cat.parent
            join fetch c.topic t
            join fetch c.user u
            """)
    Page<Course> findAllCustom(Pageable pageable);
}
