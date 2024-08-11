package com.backend.elearning.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("""
        select count(1)
        from Review r 
        join r.course c 
        join c.user u
        where (:email is null or u.email = :email)
    """)
    long findTotalReviews(@Param("email")String email);
    @Query("""
            select r
            from Review r
            left join fetch r.course c
            left join fetch r.student u
            where c.id = :courseId and r.ratingStar = :ratingStar
            """)
    Page<Review> findByRatingStarAndCourseId(@Param("ratingStar") int ratingStar, @Param("courseId") Long courseId, Pageable pageable);


    @Query("""
        select count(*)
        from Review r
        join r.course c 
        where c.id = :courseId and r.ratingStar = :ratingStar
    """)
    Long countByRatingAndCourse(@Param("ratingStar") int ratingStar, @Param("courseId") Long courseId);

    @Query("""
            select r
            from Review r
            left join fetch r.course c
            left join fetch r.student u
            where c.id = :courseId and r.status = true
            """)
    Page<Review> findByCourseId(@Param("courseId") Long courseId, Pageable pageable);


    @Query("""
            select r
            from Review r
            left join fetch r.course c
            left join fetch r.student u
            """)
    Page<Review> findAllCustom(Pageable pageable);


    @Query("""
            select r
            from Review r
            left join fetch r.course c
            left join fetch r.student u
            where LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Review> findAllCustomWithKeyword(Pageable pageable, @Param("keyword") String keyword);


    @Query("""
            select r
            from Review r
            left join fetch r.course c
            left join fetch r.student u
            where c.id = :courseId
            """)
    List<Review> findByCourseId(@Param("courseId") Long courseId);



    @Query("""
        select r 
        from Review r 
        left join fetch r.student s
        left join fetch r.course c
        where s.email = :email and c.id = :courseId
""")
    Optional<Review> findByStudentAndCourse(@Param("email") String email, @Param("courseId") Long courseId);

    @Modifying
    @Query("""
        update 
        Review s 
        set s.status = :status
        where s.id = :id
    """)
    void updateStatusReview(@Param("status") boolean status, @Param("id") Long reviewId);

}
