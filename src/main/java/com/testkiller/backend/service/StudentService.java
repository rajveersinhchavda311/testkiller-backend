package com.testkiller.backend.service;

import com.testkiller.backend.dto.student.ChangePasswordRequest;
import com.testkiller.backend.dto.student.StudentCreateRequest;
import com.testkiller.backend.dto.student.StudentResponse;
import com.testkiller.backend.dto.student.StudentUpdateRequest;
import com.testkiller.backend.entity.Course;
import com.testkiller.backend.entity.Role;
import com.testkiller.backend.entity.User;
import com.testkiller.backend.exception.BadRequestException;
import com.testkiller.backend.exception.ResourceNotFoundException;
import com.testkiller.backend.repository.CourseRepository;
import com.testkiller.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(UserRepository userRepository,
                          CourseRepository courseRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setProfilePhotoUrl(request.getProfilePhotoUrl());
        user.setRole(Role.STUDENT);
        user.setActive(true);

        if (request.getCourseId() != null) {
            user.setCourse(findCourseOrThrow(request.getCourseId()));
        }

        return toStudentResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return userRepository.findByRole(Role.STUDENT, pageable).map(this::toStudentResponse);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        return toStudentResponse(findStudentOrThrow(id));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentUpdateRequest request) {
        User user = findStudentOrThrow(id);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setProfilePhotoUrl(request.getProfilePhotoUrl());

        if (request.getCourseId() != null) {
            user.setCourse(findCourseOrThrow(request.getCourseId()));
        } else {
            user.setCourse(null);
        }

        return toStudentResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteStudent(Long id) {
        findStudentOrThrow(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = findStudentOrThrow(id);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public StudentResponse toggleStudentStatus(Long id) {
        User user = findStudentOrThrow(id);
        user.setActive(!user.isActive());
        return toStudentResponse(userRepository.save(user));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private User findStudentOrThrow(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        if (user.getRole() != Role.STUDENT) {
            throw new BadRequestException("User with id " + id + " is not a student");
        }
        return user;
    }

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    StudentResponse toStudentResponse(User u) {
        StudentResponse r = new StudentResponse();
        r.setId(u.getId());
        r.setFirstName(u.getFirstName());
        r.setLastName(u.getLastName());
        r.setEmail(u.getEmail());
        r.setPhoneNumber(u.getPhoneNumber());
        r.setGender(u.getGender());
        r.setDateOfBirth(u.getDateOfBirth());
        r.setProfilePhotoUrl(u.getProfilePhotoUrl());
        r.setRole(u.getRole());
        r.setActive(u.isActive());
        r.setCreatedAt(u.getCreatedAt());

        if (u.getCourse() != null) {
            r.setCourseId(u.getCourse().getId());
            r.setCourseName(u.getCourse().getName());
        }
        return r;
    }
}
