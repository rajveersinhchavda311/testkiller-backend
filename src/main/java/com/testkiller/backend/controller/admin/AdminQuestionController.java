package com.testkiller.backend.controller.admin;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.question.BulkQuestionRequest;
import com.testkiller.backend.dto.question.QuestionRequest;
import com.testkiller.backend.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/questions")
@Tag(name = "Admin – Questions", description = "Question bank management")
public class AdminQuestionController {

    private final QuestionService questionService;

    public AdminQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    @Operation(summary = "Add a single question")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Question created", questionService.createQuestion(request)));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Add questions in bulk")
    public ResponseEntity<ApiResponse> createBulk(@Valid @RequestBody BulkQuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Questions created", questionService.createQuestionsBulk(request)));
    }

    @GetMapping
    @Operation(summary = "Get all questions (paginated)")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Questions fetched",
                questionService.getAllQuestions(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question by ID")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Question fetched", questionService.getQuestionById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update question")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Question updated", questionService.updateQuestion(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete question by ID")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(new ApiResponse(true, "Question deleted", null));
    }
}
