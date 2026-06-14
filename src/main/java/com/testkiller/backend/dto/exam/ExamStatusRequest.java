package com.testkiller.backend.dto.exam;

import com.testkiller.backend.entity.ExamStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExamStatusRequest {

    @NotNull(message = "Status is required")
    private ExamStatus status;
}
