package com.testkiller.backend.repository;

import com.testkiller.backend.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    boolean existsByName(String name);

    @Query("SELECT COUNT(u) > 0 FROM Batch b JOIN b.students u WHERE b.id = :batchId AND u.id = :studentId")
    boolean existsStudentInBatch(@Param("batchId") Long batchId, @Param("studentId") Long studentId);
}
