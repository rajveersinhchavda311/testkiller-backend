package com.testkiller.backend.dto.student;

import com.testkiller.backend.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentUpdateRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String profilePhotoUrl;

    private Long courseId;
}
