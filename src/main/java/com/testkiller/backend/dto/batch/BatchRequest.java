package com.testkiller.backend.dto.batch;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BatchRequest {

    @NotBlank(message = "Batch name is required")
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer capacity;

    private String faculty;
}
