package com.testkiller.backend.controller.admin;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.course.CourseRequest;
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
@RequestMapping("/api/v1/admin/courses")
@Tag(name = "Admin – Courses", description = "Course management")
public class AdminCourseController {

    private final CourseService courseService;

    public AdminCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Operation(summary = "Create a course")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Course created", courseService.createCourse(request)));
    }

    @GetMapping
    @Operation(summary = "Get all courses (paginated)")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Courses fetched",
                courseService.getAllCourses(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Course fetched", courseService.getCourseById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Course updated", courseService.updateCourse(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course by ID")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new ApiResponse(true, "Course deleted", null));
    }
}
