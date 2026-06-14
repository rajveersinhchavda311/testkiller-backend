package com.testkiller.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "exam_questions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"exam_id", "question_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // display order within this exam
    @Column(nullable = false)
    private Integer questionOrder;

    // per-exam mark override; null means use question's own marks
    private Double marksOverride;
}
