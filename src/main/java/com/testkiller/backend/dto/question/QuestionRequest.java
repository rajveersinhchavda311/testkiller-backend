package com.testkiller.backend.dto.question;

import com.testkiller.backend.entity.Difficulty;
import com.testkiller.backend.entity.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    private String questionImageUrl;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    private Long subjectId;

    private String topic;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;

    @NotNull(message = "Marks are required")
    @Positive(message = "Marks must be positive")
    private Double marks;

    private Double negativeMarks = 0.0;

    private String explanation;

    private String tags;

    private Integer timeLimit;

    // for FILL_IN_BLANK and NUMERICAL
    private String correctAnswer;

    @Valid
    private List<QuestionOptionRequest> options = new ArrayList<>();
}
