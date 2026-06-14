package com.testkiller.backend.dto.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionOptionRequest {

    @NotBlank(message = "Option text is required")
    private String optionText;

    private String optionImageUrl;

    private boolean correct = false;

    private int optionOrder;
}
