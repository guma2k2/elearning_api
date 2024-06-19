package com.backend.elearning.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {




    @Query("""
            select r
            from Review r
            left join fetch r.course c
            left join fetch r.user u
            where c.id = :courseId and r.ratingStar = :ratingStar
            """)
    Page<Review> findByRatingStarAndCourseId(@Param("ratingStar") int ratingStar, @Param("courseId") Long courseId, Pageable pageable);

    @Query("""
            select r
            from Review r
            left join fetch r.course c
            left join fetch r.user u
            where c.id = :courseId
            """)
    Page<Review> findByCourseId(@Param("courseId") Long courseId, Pageable pageable);


}
