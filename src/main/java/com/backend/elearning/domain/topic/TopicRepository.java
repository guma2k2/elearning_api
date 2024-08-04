package com.backend.elearning.domain.topic;

import com.backend.elearning.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {


    @Query(value = """
            select c
            from Topic c
            where COALESCE(:keyword, '') = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Topic> findAllCustom(Pageable pageable, @Param("keyword") String keyword);

    @Query(value = """
            select count(1)
            from Topic t
            where t.name = :name and (t.id != :id or :id = null)
            """)
    Long countByNameAndId(@Param("name") String name,
                          @Param("id") Integer id);


    @Query(value = """
            select t
            from Topic t
            left join fetch t.categories
            where t.id = :id
            """)
    Optional<Topic> findByIdReturnCategories(@Param("id") Integer id);


}
