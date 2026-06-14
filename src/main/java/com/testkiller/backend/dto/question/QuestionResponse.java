package com.testkiller.backend.dto.question;

import com.testkiller.backend.entity.Difficulty;
import com.testkiller.backend.entity.QuestionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionResponse {

    private Long id;
    private String questionText;
    private String questionImageUrl;
    private QuestionType questionType;
    private Long subjectId;
    private String subjectName;
    private String topic;
    private Difficulty difficulty;
    private Double marks;
    private Double negativeMarks;
    private String explanation;
    private String tags;
    private Integer timeLimit;
    private String correctAnswer;
    private List<QuestionOptionResponse> options;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
