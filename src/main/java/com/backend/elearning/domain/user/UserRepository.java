package com.backend.elearning.domain.user;

import com.backend.elearning.domain.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT COUNT(1)
            FROM User u
            WHERE u.email = :email AND (:id is null or u.id != :id)
            """)
    Long countByExistedEmail (@Param("email") String email, @Param("id") Long id) ;
    @Query(value = """
            select c
            from User c
            where COALESCE(:keyword, '') = '' OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<User> findAllCustom(Pageable pageable, @Param("keyword") String keyword);

    @Query("""
        select u 
        from User u 
        left join fetch u.courses
        where u.id = :id
    """)
    Optional<User> findByIdCustom(@Param("id") Long id);
}
