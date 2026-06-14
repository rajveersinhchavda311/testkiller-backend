package com.testkiller.backend.controller.admin;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.student.ChangePasswordRequest;
import com.testkiller.backend.dto.student.StudentCreateRequest;
import com.testkiller.backend.dto.student.StudentUpdateRequest;
import com.testkiller.backend.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/students")
@Tag(name = "Admin – Students", description = "Student account management")
public class AdminStudentController {

    private final StudentService studentService;

    public AdminStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Create a student account")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody StudentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Student created", studentService.createStudent(request)));
    }

    @GetMapping
    @Operation(summary = "Get all students (paginated)")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Students fetched",
                studentService.getAllStudents(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Student fetched", studentService.getStudentById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student profile")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Student updated", studentService.updateStudent(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student by ID")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(new ApiResponse(true, "Student deleted", null));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Change student password (admin action)")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable Long id,
                                                       @Valid @RequestBody ChangePasswordRequest request) {
        studentService.changePassword(id, request);
        return ResponseEntity.ok(new ApiResponse(true, "Password changed", null));
    }

    @PatchMapping("/{id}/disable")
    @Operation(summary = "Toggle student active/disabled status")
    public ResponseEntity<ApiResponse> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Student status updated",
                studentService.toggleStudentStatus(id)));
    }
}
