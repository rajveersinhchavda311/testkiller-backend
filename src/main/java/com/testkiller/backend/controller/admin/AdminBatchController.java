package com.testkiller.backend.controller.admin;

import com.testkiller.backend.dto.ApiResponse;
import com.testkiller.backend.dto.batch.BatchRequest;
import com.testkiller.backend.dto.batch.BatchStudentRequest;
import com.testkiller.backend.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/batches")
@Tag(name = "Admin – Batches", description = "Batch and batch-student management")
public class AdminBatchController {

    private final BatchService batchService;

    public AdminBatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    // ── Batch CRUD ────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a batch")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody BatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Batch created", batchService.createBatch(request)));
    }

    @GetMapping
    @Operation(summary = "Get all batches (paginated)")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse(true, "Batches fetched",
                batchService.getAllBatches(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get batch by ID")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Batch fetched", batchService.getBatchById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update batch")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody BatchRequest request) {
        return ResponseEntity.ok(new ApiResponse(true, "Batch updated", batchService.updateBatch(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete batch by ID")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(new ApiResponse(true, "Batch deleted", null));
    }

    // ── Batch-Student operations ──────────────────────────────────────────────

    @PostMapping("/{batchId}/students")
    @Operation(summary = "Add student to batch")
    public ResponseEntity<ApiResponse> addStudent(@PathVariable Long batchId,
                                                   @Valid @RequestBody BatchStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Student added to batch",
                        batchService.addStudentToBatch(batchId, request)));
    }

    @DeleteMapping("/{batchId}/students/{studentId}")
    @Operation(summary = "Remove student from batch")
    public ResponseEntity<ApiResponse> removeStudent(@PathVariable Long batchId,
                                                      @PathVariable Long studentId) {
        batchService.removeStudentFromBatch(batchId, studentId);
        return ResponseEntity.ok(new ApiResponse(true, "Student removed from batch", null));
    }
}
