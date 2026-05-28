package com.testkiller.backend.service;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.ForgotPasswordRequest;
import com.testkiller.backend.dto.LoginRequest;
import com.testkiller.backend.dto.SignupRequest;
import com.testkiller.backend.dto.UserResponse;
import com.testkiller.backend.entity.Role;
import com.testkiller.backend.entity.User;
import com.testkiller.backend.exception.EmailAlreadyExistsException;
import com.testkiller.backend.exception.InactiveUserException;
import com.testkiller.backend.exception.InvalidCredentialsException;
import com.testkiller.backend.exception.UserNotFoundException;
import com.testkiller.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.STUDENT);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        UserResponse responseData = new UserResponse();
        responseData.setId(savedUser.getId());
        responseData.setFirstName(savedUser.getFirstName());
        responseData.setLastName(savedUser.getLastName());
        responseData.setEmail(savedUser.getEmail());
        responseData.setRole(savedUser.getRole());
        responseData.setActive(savedUser.isActive());
        responseData.setCreatedAt(savedUser.getCreatedAt());

        return new ApiResponse(true, "User registered successfully", responseData);
    }

    public ApiResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new InactiveUserException("User account is not active");
        }

        UserResponse responseData = new UserResponse();
        responseData.setId(user.getId());
        responseData.setFirstName(user.getFirstName());
        responseData.setLastName(user.getLastName());
        responseData.setEmail(user.getEmail());
        responseData.setRole(user.getRole());
        responseData.setActive(user.isActive());
        responseData.setCreatedAt(user.getCreatedAt());

        return new ApiResponse(true, "Login successful", responseData);
    }

    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with provided email"));

        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());

        return new ApiResponse(true,
                "Password reset instructions will be sent if the email is registered",
                data);
    }
}
