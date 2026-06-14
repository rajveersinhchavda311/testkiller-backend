package com.testkiller.backend.dto.student;

import com.testkiller.backend.entity.Gender;
import com.testkiller.backend.entity.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String profilePhotoUrl;
    private Long courseId;
    private String courseName;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
}
