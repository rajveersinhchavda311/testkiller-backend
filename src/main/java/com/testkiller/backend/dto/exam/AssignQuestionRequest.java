package com.testkiller.backend.dto.exam;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignQuestionRequest {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    // optional: specify position; if null, appended at end
    private Integer questionOrder;

    // optional: override per-exam marks; if null, uses question's own marks
    private Double marksOverride;
}
