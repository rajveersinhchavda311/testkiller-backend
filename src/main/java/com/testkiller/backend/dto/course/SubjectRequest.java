package com.testkiller.backend.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectRequest {

    @NotBlank(message = "Subject name is required")
    private String name;

    private String description;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
