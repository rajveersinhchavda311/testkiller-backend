package com.testkiller.backend.dto.course;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseResponse {

    private Long id;
    private String name;
    private String description;
    private String duration;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
