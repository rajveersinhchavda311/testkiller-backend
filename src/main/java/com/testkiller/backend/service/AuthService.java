package com.testkiller.backend.service;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.AuthResponse;
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
import com.testkiller.backend.security.JwtUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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

        UserResponse responseData = toUserResponse(savedUser);
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

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );

        String token = jwtUtil.generateToken(userDetails);
        UserResponse userResponse = toUserResponse(user);
        AuthResponse authResponse = new AuthResponse(token, jwtUtil.getExpirationMs(), userResponse);

        return new ApiResponse(true, "Login successful", authResponse);
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

    private UserResponse toUserResponse(User user) {
        UserResponse r = new UserResponse();
        r.setId(user.getId());
        r.setFirstName(user.getFirstName());
        r.setLastName(user.getLastName());
        r.setEmail(user.getEmail());
        r.setRole(user.getRole());
        r.setActive(user.isActive());
        r.setCreatedAt(user.getCreatedAt());
        return r;
    }
}
