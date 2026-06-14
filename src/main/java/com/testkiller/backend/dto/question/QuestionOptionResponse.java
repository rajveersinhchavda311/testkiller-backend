package com.testkiller.backend.dto.question;

import lombok.Data;

@Data
public class QuestionOptionResponse {

    private Long id;
    private String optionText;
    private String optionImageUrl;
    private boolean correct;
    private int optionOrder;
}
