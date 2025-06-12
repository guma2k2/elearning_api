package com.backend.elearning.domain.order;

import com.backend.elearning.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        update Order o
        set o.status =:status
        where o.id = :orderId
    """)
    @Modifying
    void updateOrderStatus(@Param("orderId") Long orderId, @Param("status") EOrderStatus status);

    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon c
            left join fetch o.orderDetails 
            where s.email =:email
            order by o.createdAt desc 
        """)
    List<Order> findAllByStudent(@Param("email")String email);

    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon c
            left join fetch o.orderDetails 
            where s.email =:email and o.status = :status
        """)
    List<Order> findAllByStudentAndStatus(@Param("email")String email,
                                          @Param("status") EOrderStatus status);

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
            and (:date IS NULL or DATE(o.createdAt) = DATE(:date))
        """)
    Page<Order> findAllCustomWithId(Pageable pageable, @Param("orderId") Long orderId,
                                    @Param("date")String date);



    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon 
            left join fetch o.orderDetails 
            where o.status = :status 
            and (:date IS NULL or DATE(o.createdAt) = DATE(:date))
        """)
    Page<Order> findAllCustomWithStatus(Pageable pageable, @Param("status") EOrderStatus status,
                                        @Param("date")String date);


    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon 
            left join fetch o.orderDetails 
            where o.status = :status and o.id = :orderId
            and (:date IS NULL or DATE(o.createdAt) = DATE(:date))
        """)
    Page<Order> findAllCustomWithStatusAndId(Pageable pageable,
                                             @Param("status") EOrderStatus status,
                                             @Param("orderId") Long orderId,
                                             @Param("date")String date

    );


    @Query("""
        select count(1)
        from Order 
    """)
    long findTotalOrders();

    @Query("""
            select o
            from Order o 
            left join fetch o.student s 
            left join fetch o.coupon 
            left join fetch o.orderDetails 
            where (:date IS NULL or DATE(o.createdAt) = DATE(:date))
        """)
    Page<Order> findAllCustomAndDate(Pageable pageable, @Param("date")String date);
}
