package com.testkiller.backend.dto.course;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubjectResponse {

    private Long id;
    private String name;
    private String description;
    private Long courseId;
    private String courseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
