package com.backend.elearning.domain.classroom;

import com.backend.elearning.domain.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {


    @Query("""
        select c 
        from Classroom c 
        join c.course co 
        join co.user 
        where co.id = :courseId
""")
    List<Classroom> findByCourseId(@Param("courseId") Long courseId);


    @Query(value = """
            select s
            from Classroom s
            left join fetch s.meetings
            join s.course c
            join c.user
            where s.id = :id
            """)
    Optional<Classroom> findByIdMeetings(@Param("id") Long id);

    @Query(value = """
            select distinct s
            from Classroom s
            left join fetch s.references
            where s = :classroom
            """)
    Optional<Classroom> findByIdReferences(@Param("classroom") Classroom classroom);

    @Query(value = """
            select distinct s
            from Classroom s
            left join fetch s.exercises
            where s = :classroom
            """)
    Optional<Classroom> findByIdExercises(@Param("classroom") Classroom classroom);
}
