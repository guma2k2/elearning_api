package com.backend.elearning.domain.order;

import com.backend.elearning.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
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
            left join fetch o.orderDetails 
            where s.email =:email
        """)
    List<Order> findAllByStudent(@Param("email")String email);
}
