package com.backend.elearning.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {


    @Query("""
            select c 
            from Cart c
            left join fetch c.user u
            left join fetch c.course c
            where u.email = :email and c.id = :courseId
            """)
    Optional<Cart> findByEmailAndCourseId(@Param("courseId") Long courseId, @Param("email") String email);


    @Query("""
            select c 
            from Cart c
            left join fetch c.user u
            left join fetch c.course c
            where u.email = :email
            """)
    List<Cart> findByUserEmail(@Param("email") String email);
}
