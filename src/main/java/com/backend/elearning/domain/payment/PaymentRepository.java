package com.backend.elearning.domain.payment;

import com.backend.elearning.domain.statitic.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @Query("""
        SELECT new com.backend.elearning.domain.statitic.Statistic(
                      EXTRACT(MONTH FROM p.payDate),
                      SUM(p.amount)
                  )
        FROM Payment p
        WHERE
            (EXTRACT(YEAR FROM p.payDate) = :year)
        GROUP BY EXTRACT(MONTH FROM p.payDate)
    """)
    List<Statistic> findByYear(@Param("year") int year);



    @Query("""
        SELECT new com.backend.elearning.domain.statitic.Statistic(
                      EXTRACT(DAY FROM p.payDate),
                      SUM(p.amount)
                  )
        FROM Payment p
        WHERE EXTRACT(MONTH FROM p.payDate) = :month 
            AND EXTRACT(YEAR FROM p.payDate) = :year
        GROUP BY EXTRACT(DAY FROM p.payDate)
    """)
    List<Statistic> findByMonthAndYear(@Param("month") int month,
                                       @Param("year") int year
    );


}
