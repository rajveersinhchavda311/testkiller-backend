package com.testkiller.backend.repository;

import com.testkiller.backend.entity.Difficulty;
import com.testkiller.backend.entity.Question;
import com.testkiller.backend.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findBySubjectId(Long subjectId, Pageable pageable);

    Page<Question> findByDifficulty(Difficulty difficulty, Pageable pageable);

    Page<Question> findByQuestionType(QuestionType questionType, Pageable pageable);
}
