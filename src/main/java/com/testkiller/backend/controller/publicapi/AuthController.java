package com.testkiller.backend.controller.publicapi;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.ForgotPasswordRequest;
import com.testkiller.backend.dto.LoginRequest;
import com.testkiller.backend.dto.SignupRequest;
import com.testkiller.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/auth")
@Tag(name = "Authentication", description = "Authentication and session management endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody SignupRequest request) {
        ApiResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse response = authService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request a password reset")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ApiResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }
}
