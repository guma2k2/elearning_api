package com.backend.elearning.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
            left join fetch c.student u
            left join fetch c.course co
            where u.email = :email and co.id = :courseId
            """)
    Optional<Cart> findByEmailAndCourseId(@Param("courseId") Long courseId, @Param("email") String email);


    @Query("""
            select c 
            from Cart c
            left join fetch c.student u
            left join fetch c.course co
            where u.email = :email
            """)
    List<Cart> findByUserEmail(@Param("email") String email);

    @Query("""
            select c 
            from Cart c
            left join fetch c.student u
            left join fetch c.course co
            where u.email = :email and c.buyLater = false
            """)
    List<Cart> findByUserEmailWithBuyLater(@Param("email") String email);


    @Modifying
    @Query("""
        update Cart c
        set c.buyLater = :buyLater
        where c.id = :cartId
    """)
    void updateCartBuyLater(@Param("buyLater") boolean buyLater,
                            @Param("cartId") Long cartId);


    @Query("""
        select c 
        from Cart c 
        join c.course co
        where co.id = :courseId
""")
    List<Cart> findByCourseId(@Param("courseId") Long courseId);

}
