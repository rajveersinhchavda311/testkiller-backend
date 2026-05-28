package com.testkiller.backend.controller.publicapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicTestController {

    @GetMapping("/api/v1/public/test")
    public String publicTest() {
        return "Public API Working";
    }
}