package com.backend.elearning.domain.referencefile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenceFileRepo extends JpaRepository<ReferenceFile, Long> {


    @Query("""
            select r 
            from ReferenceFile r
            join r.reference re
            where re.id = :referenceId
            """)
    List<ReferenceFile> findByReferenceId(@Param("referenceId") Long referenceId);
}
