package com.backend.elearning.domain.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);




    @Query("""
        select count(1)
        from Coupon c 
        where c.code = :code and ( c.id != :id or :id = null )
    """)
    long findByCodeAndId(@Param("code") String code, @Param("id") Long couponId);


    @Query("""
        select c
        from Coupon c 
        left join fetch c.orders 
        where c.id = :couponId
    """)
    Optional<Coupon> findByIdCustom(@Param("couponId") Long couponId);
}
