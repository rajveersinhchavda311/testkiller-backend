package com.testkiller.backend.repository;

import com.testkiller.backend.entity.Exam;
import com.testkiller.backend.entity.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    Page<Exam> findByStatus(ExamStatus status, Pageable pageable);
}
