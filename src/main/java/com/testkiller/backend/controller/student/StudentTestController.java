package com.testkiller.backend.controller.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentTestController {

    @GetMapping("/api/v1/student/test")
    public String studentTest() {
        return "Student API Working";
    }
}