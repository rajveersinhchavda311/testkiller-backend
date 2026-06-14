package com.testkiller.backend.repository;

import com.testkiller.backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByName(String name);
}
