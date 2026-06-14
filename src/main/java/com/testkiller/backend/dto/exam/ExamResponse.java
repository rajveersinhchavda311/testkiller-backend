package com.testkiller.backend.dto.exam;

import com.testkiller.backend.entity.ExamStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamResponse {

    private Long id;
    private String title;
    private String description;
    private String instructions;
    private Long subjectId;
    private String subjectName;
    private Long courseId;
    private String courseName;
    private Integer totalMarks;
    private Integer passingMarks;
    private Integer duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxAttempts;
    private boolean shuffleQuestions;
    private boolean shuffleOptions;
    private boolean negativeMarking;
    private ExamStatus status;
    private int questionCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
