package com.testkiller.backend.dto.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank(message = "Course name is required")
    private String name;

    private String description;

    private String duration;

    private boolean active = true;
}
