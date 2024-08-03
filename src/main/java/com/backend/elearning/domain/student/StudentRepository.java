package com.backend.elearning.domain.student;

import com.backend.elearning.domain.topic.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    @Query("""
        select count(1)
        from Student 
    """)
    long findTotalStudents();

    @Modifying
    @Query("""
        update 
        Student s 
        set s.active = :status
        where s.id = :id
    """)
    void updateStatusStudent(@Param("status") boolean status, @Param("id") Long studentId);

    @Query(value = """
            select c
            from Student c
            where (:keyword IS NULL or LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Student> findAllCustom(Pageable pageable, @Param("keyword") String keyword);
}
