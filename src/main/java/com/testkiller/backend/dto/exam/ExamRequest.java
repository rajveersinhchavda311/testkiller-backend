package com.testkiller.backend.dto.exam;

import com.testkiller.backend.entity.ExamStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamRequest {

    @NotBlank(message = "Exam title is required")
    private String title;

    private String description;

    private String instructions;

    private Long subjectId;

    private Long courseId;

    private Integer totalMarks;

    private Integer passingMarks;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer duration;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer maxAttempts = 1;

    private boolean shuffleQuestions = false;

    private boolean shuffleOptions = false;

    private boolean negativeMarking = false;

    private ExamStatus status = ExamStatus.DRAFT;
}
