package com.testkiller.backend.repository;

import com.testkiller.backend.entity.ExamQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    boolean existsByExamIdAndQuestionId(Long examId, Long questionId);

    Optional<ExamQuestion> findByExamIdAndQuestionId(Long examId, Long questionId);

    Page<ExamQuestion> findByExamId(Long examId, Pageable pageable);

    boolean existsByQuestionId(Long questionId);

    long countByExamId(Long examId);

    @Query("SELECT COALESCE(MAX(eq.questionOrder), 0) FROM ExamQuestion eq WHERE eq.exam.id = :examId")
    int findMaxOrderByExamId(@Param("examId") Long examId);

    void deleteByExamIdAndQuestionId(Long examId, Long questionId);
}
