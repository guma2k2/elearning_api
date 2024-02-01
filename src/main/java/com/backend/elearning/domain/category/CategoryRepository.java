package com.backend.elearning.domain.category;

import com.backend.elearning.domain.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {



    @Query(value = """
            select count(1)
            from Category c
            where c.name = :name and (c.id != :id or :id = null)
            """)
    Long countExistByName (@Param("name") String name,
                           @Param("id") Integer id);


    @Query(value = """
            select c
            from Category c
            where c.id = :id
            """)
    Optional<Category> findByIdWithParent(@Param("id") Integer id);



    @Query(value = """
            select c
            from Category c
            left join fetch c.childrenList
            where c.parent IS NULL
            """)
    List<Category> findAllParents();

    Optional<Category> findByName(String catName);

    Set<Category> findAllByNameIn(List<String> catsName);
}
