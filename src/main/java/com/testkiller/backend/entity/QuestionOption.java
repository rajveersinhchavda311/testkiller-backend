package com.testkiller.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_options")
@Getter
@Setter
@NoArgsConstructor
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionText;

    private String optionImageUrl;

    @Column(nullable = false)
    private boolean correct = false;

    // 0-based position (0=A, 1=B, 2=C, 3=D)
    @Column(nullable = false)
    private int optionOrder;
}
