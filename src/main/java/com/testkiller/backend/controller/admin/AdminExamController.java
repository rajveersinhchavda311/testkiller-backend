package com.testkiller.backend.controller.admin;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.exam.AssignQuestionRequest;
import com.testkiller.backend.dto.exam.ExamRequest;
import com.testkiller.backend.dto.exam.ExamStatusRequest;
import com.testkiller.backend.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/exams")
@Tag(name = "Admin – Exams", description = "Exam and question-assignment management")
public class AdminExamController {

    private final ExamService examService;

    public AdminExamController(ExamService examService) {
        this.examService = examService;
    }

    // ── Exam CRUD ─────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create an exam")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Exam created", examService.createExam(request)));
    }

    @GetMapping
    @Operation(summary = "Get all exams (paginated)")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Exams fetched",
                examService.getAllExams(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active exams")
    public ResponseEntity<ApiResponse> getActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Active exams fetched",
                examService.getActiveExams(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get exam by ID")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Exam fetched", examService.getExamById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update exam")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody ExamRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Exam updated", examService.updateExam(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete exam by ID")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.ok(new ApiResponse(true, "Exam deleted", null));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Mark exam as ACTIVE, INACTIVE, DRAFT, or COMPLETED")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id,
                                                     @Valid @RequestBody ExamStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Exam status updated",
                examService.updateExamStatus(id, request)));
    }

    // ── Question assignment ───────────────────────────────────────────────────

    @PostMapping("/{examId}/questions")
    @Operation(summary = "Assign a question to an exam")
    public ResponseEntity<ApiResponse> assignQuestion(@PathVariable Long examId,
                                                       @Valid @RequestBody AssignQuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Question assigned to exam",
                        examService.assignQuestion(examId, request)));
    }

    @DeleteMapping("/{examId}/questions/{questionId}")
    @Operation(summary = "Remove a question from an exam")
    public ResponseEntity<ApiResponse> removeQuestion(@PathVariable Long examId,
                                                       @PathVariable Long questionId) {
        examService.removeQuestion(examId, questionId);
        return ResponseEntity.ok(new ApiResponse(true, "Question removed from exam", null));
    }

    @GetMapping("/{examId}/questions")
    @Operation(summary = "Get all questions assigned to an exam")
    public ResponseEntity<ApiResponse> getExamQuestions(
            @PathVariable Long examId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Exam questions fetched",
                examService.getExamQuestions(examId,
                        PageRequest.of(page, size, Sort.by("questionOrder").ascending()))));
    }
}
