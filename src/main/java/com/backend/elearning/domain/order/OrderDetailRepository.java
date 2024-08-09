package com.backend.elearning.domain.order;

import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.statitic.Statistic;
import com.backend.elearning.domain.statitic.StatisticCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {



    @Query("""
        select count(1) 
        from OrderDetail  od 
        join od.course c 
        join c.user 
        where c.user.email = :email
    """)
    long countByInstructor(@Param("email") String email);
    @Query("""
            select od.course.id
            from OrderDetail od
            GROUP BY od.course.id
            ORDER BY COUNT(od.course.id) DESC
        """)
    Page<Long> getBestSellerCourses(Pageable pageable);


    @Query("""
        select od 
        from OrderDetail od 
        join fetch od.order o 
        left join fetch od.course
        where o.id = :orderId
    """)
    List<OrderDetail> findByOrderId (@Param("orderId") Long orderId);
    @Query("""
        SELECT new com.backend.elearning.domain.statitic.Statistic(
                      EXTRACT(MONTH FROM o.createdAt),
                      SUM(od.price)
                  )
        FROM OrderDetail od
        join od.order o
        join od.course c 
        join c.user u 
        WHERE 
            (EXTRACT(YEAR FROM o.createdAt) = :year)
            AND u.email = :email
        GROUP BY EXTRACT(MONTH FROM o.createdAt)
    """)
    List<Statistic> findByYearAndEmail(@Param("year") int year, @Param("email")String email);

    @Query("""
        SELECT new com.backend.elearning.domain.statitic.Statistic(
                      EXTRACT(DAY FROM o.createdAt),
                      SUM(od.price)
                  )
        FROM OrderDetail od 
        join od.order o
        join od.course c 
        join c.user u 
        WHERE EXTRACT(MONTH FROM o.createdAt) = :month 
            AND EXTRACT(YEAR FROM o.createdAt) = :year
            AND u.email = :email
            and o.status = 'SUCCESS'
        GROUP BY EXTRACT(DAY FROM o.createdAt)
    """)
    List<Statistic> findByMonthAndYearAndEmail(@Param("month") int month,
                                       @Param("year") int year, @Param("email")String email
    );

    @Query("""
        select new com.backend.elearning.domain.statitic.StatisticCourse(od.course.title, count(*), sum(od.price))
        from OrderDetail od
        join od.order o
        join od.course c
        join c.user s
        where (:email is null or s.email = :email)
            and o.createdAt between :from and :to
            and o.status = 'SUCCESS'
        group by c.title
    """)
    List<StatisticCourse> getStatisticByTime (@Param("from")LocalDateTime from,
                                              @Param("to")LocalDateTime to,
                                              @Param("email") String email);

    @Query("""
        select c 
        from OrderDetail c 
        join c.course co
        where co.id = :courseId
    """)
    List<OrderDetail> findByCourseId(@Param("courseId") Long courseId);
}
