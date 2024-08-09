package com.backend.elearning.domain.order;

import com.backend.elearning.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon c
            left join fetch o.orderDetails 
            where s.email =:email
        """)
    List<Order> findAllByStudent(@Param("email")String email);

    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon c
            left join fetch o.orderDetails 
            where o.id = :id
        """)
    Optional<Order> findByIdCustom(@Param("id")Long orderId);

    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon 
            left join fetch o.orderDetails 
        """)
    Page<Order> findAllCustom(Pageable pageable);

    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon 
            left join fetch o.orderDetails 
            where o.id = :orderId
        """)
    Page<Order> findAllCustomWithId(Pageable pageable, @Param("orderId") Long orderId);


    @Query("""
        update Order o
        set o.status =:status
        where o.id = :orderId
    """)
    @Modifying
    void updateOrderStatus(@Param("orderId") Long orderId, @Param("status") EOrderStatus status);


    @Query("""
        select count(1)
        from Order 
    """)
    long findTotalOrders();



}
