package com.testkiller.backend.service;

import com.testkiller.backend.dto.batch.BatchRequest;
import com.testkiller.backend.dto.batch.BatchResponse;
import com.testkiller.backend.dto.batch.BatchStudentRequest;
import com.testkiller.backend.entity.Batch;
import com.testkiller.backend.entity.Role;
import com.testkiller.backend.entity.User;
import com.testkiller.backend.exception.BadRequestException;
import com.testkiller.backend.exception.ResourceNotFoundException;
import com.testkiller.backend.repository.BatchRepository;
import com.testkiller.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final UserRepository userRepository;

    public BatchService(BatchRepository batchRepository, UserRepository userRepository) {
        this.batchRepository = batchRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BatchResponse createBatch(BatchRequest request) {
        if (batchRepository.existsByName(request.getName())) {
            throw new BadRequestException("Batch with name '" + request.getName() + "' already exists");
        }
        Batch batch = new Batch();
        applyRequest(batch, request);
        return toBatchResponse(batchRepository.save(batch));
    }

    @Transactional(readOnly = true)
    public Page<BatchResponse> getAllBatches(Pageable pageable) {
        return batchRepository.findAll(pageable).map(this::toBatchResponse);
    }

    @Transactional(readOnly = true)
    public BatchResponse getBatchById(Long id) {
        return toBatchResponse(findBatchOrThrow(id));
    }

    @Transactional
    public BatchResponse updateBatch(Long id, BatchRequest request) {
        Batch batch = findBatchOrThrow(id);
        if (!batch.getName().equals(request.getName()) && batchRepository.existsByName(request.getName())) {
            throw new BadRequestException("Batch with name '" + request.getName() + "' already exists");
        }
        applyRequest(batch, request);
        return toBatchResponse(batchRepository.save(batch));
    }

    @Transactional
    public void deleteBatch(Long id) {
        findBatchOrThrow(id);
        batchRepository.deleteById(id);
    }

    @Transactional
    public BatchResponse addStudentToBatch(Long batchId, BatchStudentRequest request) {
        Batch batch = findBatchOrThrow(batchId);
        User student = findStudentOrThrow(request.getStudentId());

        if (batchRepository.existsStudentInBatch(batchId, student.getId())) {
            throw new BadRequestException("Student is already in this batch");
        }
        if (batch.getCapacity() != null && batch.getStudents().size() >= batch.getCapacity()) {
            throw new BadRequestException("Batch is at full capacity (" + batch.getCapacity() + ")");
        }

        batch.getStudents().add(student);
        return toBatchResponse(batchRepository.save(batch));
    }

    @Transactional
    public void removeStudentFromBatch(Long batchId, Long studentId) {
        Batch batch = findBatchOrThrow(batchId);
        User student = findStudentOrThrow(studentId);

        if (!batchRepository.existsStudentInBatch(batchId, studentId)) {
            throw new BadRequestException("Student is not in this batch");
        }

        batch.getStudents().remove(student);
        batchRepository.save(batch);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(Batch batch, BatchRequest request) {
        batch.setName(request.getName());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setCapacity(request.getCapacity());
        batch.setFaculty(request.getFaculty());
    }

    private Batch findBatchOrThrow(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + id));
    }

    private User findStudentOrThrow(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        if (user.getRole() != Role.STUDENT) {
            throw new BadRequestException("User with id " + id + " is not a student");
        }
        return user;
    }

    private BatchResponse toBatchResponse(Batch b) {
        BatchResponse r = new BatchResponse();
        r.setId(b.getId());
        r.setName(b.getName());
        r.setStartDate(b.getStartDate());
        r.setEndDate(b.getEndDate());
        r.setCapacity(b.getCapacity());
        r.setFaculty(b.getFaculty());
        r.setStudentCount(b.getStudents().size());
        r.setCreatedAt(b.getCreatedAt());
        r.setUpdatedAt(b.getUpdatedAt());
        return r;
    }
}
