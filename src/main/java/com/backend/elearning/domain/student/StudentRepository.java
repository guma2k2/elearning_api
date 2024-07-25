package com.backend.elearning.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);


    @Modifying
    @Query("""
        update 
        Student s 
        set s.active = :status
        where s.id = :id
    """)
    void updateStatusStudent(@Param("status") boolean status, @Param("id") Long studentId);
}
