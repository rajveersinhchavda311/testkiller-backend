package com.testkiller.backend.repository;

import com.testkiller.backend.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsByNameAndCourseId(String name, Long courseId);

    boolean existsByCourseId(Long courseId);

    Page<Subject> findByCourseId(Long courseId, Pageable pageable);
}
