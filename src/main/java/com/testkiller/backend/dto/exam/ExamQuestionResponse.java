package com.testkiller.backend.dto.exam;

import com.testkiller.backend.dto.question.QuestionResponse;
import lombok.Data;

@Data
public class ExamQuestionResponse {

    private Long id;
    private Integer questionOrder;
    private Double marksOverride;
    private QuestionResponse question;
}
