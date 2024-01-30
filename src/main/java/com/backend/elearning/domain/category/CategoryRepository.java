package com.backend.elearning.domain.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {


    @Query(value = """
            select c
            from Category c
            join fetch c.parent p
            """)
    Page<Category> findAllWithParent(Pageable pageable);

    @Query(value = """
            select count(1)
            from Category c
            join fetch c.parent p
            where c.name = :name and (c.id != :id or :id = null)
            """)
    Long countExistByName (@Param("name") String name,
                           @Param("id") Integer id);


    @Query(value = """
            select c
            from Category c
            join fetch c.parent p
            where c.id = :id
            """)
    Optional<Category> findByIdWithParent(@Param("id") Integer id);
}
