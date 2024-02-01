package com.backend.elearning.domain.topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {


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
