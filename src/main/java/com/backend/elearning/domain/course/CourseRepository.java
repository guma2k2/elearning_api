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

    @Query("""
        select count(1)
        from Course c 
        join c.user u
        where (:email is null or u.email = :email)
    """)
    long findTotalCourses(@Param("email")String email);
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
            join fetch c.category ca
            join fetch ca.parent
            join fetch c.topic
            where c.id = :id
            """)
    Optional<Course> findByIdReturnSections(@Param("id") Long courseId);

    @Query(value = """
            select c
            from Course c
            left join fetch c.sections s
            join fetch c.category cat
            left join fetch cat.parent
            join fetch c.topic t
            where c.slug = :slug
            """)
    Optional<Course> findBySlugReturnSections(@Param("slug") String slug);


    @Query(value = """
            select c
            from Course c
            join fetch c.category cat
            left join fetch cat.parent
            join fetch c.topic t
            join fetch c.user u
            """)
    Page<Course> findAllCustom(Pageable pageable);

    @Query(value = """
            select c
            from Course c
            join fetch c.category cat
            left join fetch cat.parent
            join fetch c.topic t
            join fetch c.user u
            where c.title like %:title%
            """)
    Page<Course> findAllCustom(Pageable pageable, @Param("title") String title);


    @Query(value = """
        select c
        from Course c
        join fetch c.category cat
        left join fetch cat.parent p
        join fetch c.topic t
        join fetch c.user u
        left join fetch c.reviews
        where (:title IS NULL or LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))
        and (:level IS NULL or c.level in :level)
        and (:free IS NULL or c.free in :free)
        and (:categoryName IS NULL or cat.name = :categoryName or p.name = :categoryName)
        and (:topicId IS NULL or t.id = :topicId)
        and (:ratingStar IS NULL or
             (select avg(r.ratingStar)
              from Review r
              join r.course rc 
              where rc.id = c.id 
              group by rc.id) >= :ratingStar)
    """)
    Page<Course> findByMultiQuery(Pageable pageable,
                                  @Param("title") String title,
                                  @Param("ratingStar") Float ratingStar,
                                  @Param("level") String[] level,
                                  @Param("free") Boolean[] free,
                                  @Param("categoryName") String categoryName,
                                  @Param("topicId") Integer topicId
                                  );

    // group by c.id, cat.id, p.id, t.id, u.id, r.id
    //            HAVING COALESCE(AVG(r.ratingStar), 0) > :ratingStar
}
