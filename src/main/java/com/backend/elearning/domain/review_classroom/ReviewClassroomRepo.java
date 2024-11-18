package com.backend.elearning.domain.review_classroom;

import com.backend.elearning.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewClassroomRepo extends JpaRepository<ReviewClassroom, Long> {

    @Query("""
            select r
            from ReviewClassroom r
            left join fetch r.classroom c
            left join fetch r.student u
            where c.id = :classroomId
            """)
    List<ReviewClassroom> findByClassroom(@Param("classroomId") Long classroomId);


    @Query("""
            select r
            from ReviewClassroom r
            left join fetch r.student u
            where u.email = :email
            """)
    Optional<ReviewClassroom> findByStudent(@Param("email") String email);
}
