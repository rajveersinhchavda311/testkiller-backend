package com.testkiller.backend.dto.batch;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BatchStudentRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;
}
