package com.testkiller.backend.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminTestController {

    @GetMapping("/api/v1/admin/test")
    public String adminTest() {
        return "Admin API Working";
    }
}