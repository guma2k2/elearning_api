package com.backend.elearning.domain.course;

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
            where c.title = :title and (:id is  null or c.id != :id )
            """)
    Long countExistByTitle(@Param("title") String title,
                           @Param("id") Long id);


    @Query(value = """
            select c
            from Course c
            left join fetch c.sections s
            join fetch c.category ca
            left join fetch ca.parent
            join fetch c.topic
            join fetch c.user
            where c.id = :id
            """)
    Optional<Course> findByIdReturnSections(@Param("id") Long courseId);

    @Query(value = """
            select c
            from Course c
            left join fetch c.sections s
            join fetch c.category ca
            left join fetch ca.parent
            join fetch c.topic
            where ca.id = :id
            """)
    List<Course> findByCategoryId(@Param("id") Integer categoryId);
    @Query(value = """
            select c
            from Course c
            left join fetch c.sections s
            join fetch c.category ca
            left join fetch ca.parent
            join fetch c.topic
            where ca.id = :id and c.publish = true
            """)
    List<Course> findByCategoryIdWithStatus(@Param("id") Integer categoryId);

    @Query(value = """
            select c
            from Course c
            left join fetch c.sections s
            join fetch c.category ca
            join fetch c.topic t
            left join fetch ca.parent
            join fetch c.topic
            where t.id = :id
            """)
    List<Course> findByTopicId(@Param("id") Integer topicId);

    @Query(value = """
            select c
            from Course c
            join c.user u
            left join fetch c.sections s
            join fetch c.category ca
            left join fetch ca.parent
            join fetch c.topic
            where u.id = :userId
            """)
    List<Course> findByUserIdReturnSections(@Param("userId") Long userId);

    @Query(value = """
            select c
            from Course c
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
            where (:email is null or u.email = :email)
            and COALESCE(:keyword, '') = '' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Course> findAllCustomByRole(Pageable pageable, @Param("email") String email, @Param("keyword")String keyword);

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
        where (:level IS NULL or c.level in :level)
        and (:free IS NULL or c.free in :free)
        and (:categoryName IS NULL or cat.name = :categoryName or p.name = :categoryName)
        and (:topicId IS NULL or t.id = :topicId)
        and (:ratingStar IS NULL or
             (select avg(r.ratingStar)
              from Review r
              join r.course rc 
              where rc.id = c.id 
              group by rc.id) >= :ratingStar)
        and c.publish = true 
    """)
    Page<Course> findByMultiQuery(Pageable pageable,
                                  @Param("ratingStar") Float ratingStar,
                                  @Param("level") String[] level,
                                  @Param("free") Boolean[] free,
                                  @Param("categoryName") String categoryName,
                                  @Param("topicId") Integer topicId
                                  );
    @Query(value = """
        select c
        from Course c
        join fetch c.category cat
        left join fetch cat.parent p
        join fetch c.topic t
        join fetch c.user u
        left join fetch c.reviews
        where (:level IS NULL or c.level in :level)
        and (:free IS NULL or c.free in :free)
        and (:categoryName IS NULL or cat.name = :categoryName or p.name = :categoryName)
        and (:topicId IS NULL or t.id = :topicId)
        and (:ratingStar IS NULL or
             (select avg(r.ratingStar)
              from Review r
              join r.course rc 
              where rc.id = c.id 
              group by rc.id) >= :ratingStar)
        and c.publish = true 
    """)
    List<Course> findByMultiQuery(
                                  @Param("ratingStar") Float ratingStar,
                                  @Param("level") String[] level,
                                  @Param("free") Boolean[] free,
                                  @Param("categoryName") String categoryName,
                                  @Param("topicId") Integer topicId
    );
    @Query(value = """
        select c
        from Course c
        join fetch c.category cat
        left join fetch cat.parent p
        join fetch c.topic t
        join fetch c.user u
        left join fetch c.reviews
        where LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))
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
        and c.publish = true 
    """)
    Page<Course> findByMultiQueryWithKeyword(Pageable pageable,
                                  @Param("title") String title,
                                  @Param("ratingStar") Float ratingStar,
                                  @Param("level") String[] level,
                                  @Param("free") Boolean[] free,
                                  @Param("categoryName") String categoryName,
                                  @Param("topicId") Integer topicId
    );


    @Query(value = """
        select c
        from Course c
        join fetch c.category cat
        left join fetch cat.parent p
        join fetch c.topic t
        join fetch c.user u
        left join fetch c.reviews
        where LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))
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
        and c.publish = true 
    """)
    List<Course> findByMultiQueryWithKeyword(
                                             @Param("title") String title,
                                             @Param("ratingStar") Float ratingStar,
                                             @Param("level") String[] level,
                                             @Param("free") Boolean[] free,
                                             @Param("categoryName") String categoryName,
                                             @Param("topicId") Integer topicId
    );
    @Modifying
    @Query("""
        update 
        Course s 
        set s.publish = :status
        where s.id = :id
    """)
    void updateStatusCourse(@Param("status") boolean status, @Param("id") Long courseId);
}
