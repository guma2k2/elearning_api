package com.backend.elearning.domain.order;

import com.backend.elearning.domain.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {


    @Query("""
            select od.course.id
            from OrderDetail od
            GROUP BY od.course.id
            ORDER BY COUNT(od.course.id) DESC
        """)
    Page<Long> getBestSellerCourses(Pageable pageable);
}