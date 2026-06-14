package com.testkiller.backend.controller.admin;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.course.SubjectRequest;
import com.testkiller.backend.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/subjects")
@Tag(name = "Admin – Subjects", description = "Subject management")
public class AdminSubjectController {

    private final CourseService courseService;

    public AdminSubjectController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Operation(summary = "Create a subject")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Subject created", courseService.createSubject(request)));
    }

    @GetMapping
    @Operation(summary = "Get all subjects (optionally filter by courseId)")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Subjects fetched",
                courseService.getAllSubjects(courseId,
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Subject fetched", courseService.getSubjectById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Subject updated", courseService.updateSubject(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject by ID")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        courseService.deleteSubject(id);
        return ResponseEntity.ok(new ApiResponse(true, "Subject deleted", null));
    }
}
