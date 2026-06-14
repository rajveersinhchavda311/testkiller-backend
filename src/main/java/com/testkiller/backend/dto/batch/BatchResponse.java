package com.testkiller.backend.dto.batch;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BatchResponse {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer capacity;
    private String faculty;
    private int studentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
