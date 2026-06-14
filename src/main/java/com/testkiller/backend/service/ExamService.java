package com.testkiller.backend.service;

import com.testkiller.backend.dto.exam.AssignQuestionRequest;
import com.testkiller.backend.dto.exam.ExamQuestionResponse;
import com.testkiller.backend.dto.exam.ExamRequest;
import com.testkiller.backend.dto.exam.ExamResponse;
import com.testkiller.backend.dto.exam.ExamStatusRequest;
import com.testkiller.backend.entity.Course;
import com.testkiller.backend.entity.Exam;
import com.testkiller.backend.entity.ExamQuestion;
import com.testkiller.backend.entity.ExamStatus;
import com.testkiller.backend.entity.Question;
import com.testkiller.backend.entity.Subject;
import com.testkiller.backend.exception.BadRequestException;
import com.testkiller.backend.exception.ResourceNotFoundException;
import com.testkiller.backend.repository.CourseRepository;
import com.testkiller.backend.repository.ExamQuestionRepository;
import com.testkiller.backend.repository.ExamRepository;
import com.testkiller.backend.repository.QuestionRepository;
import com.testkiller.backend.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final QuestionService questionService;

    public ExamService(ExamRepository examRepository,
                       SubjectRepository subjectRepository,
                       CourseRepository courseRepository,
                       QuestionRepository questionRepository,
                       ExamQuestionRepository examQuestionRepository,
                       QuestionService questionService) {
        this.examRepository = examRepository;
        this.subjectRepository = subjectRepository;
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.questionService = questionService;
    }

    // ── Exam CRUD ─────────────────────────────────────────────────────────────

    @Transactional
    public ExamResponse createExam(ExamRequest request) {
        Exam exam = new Exam();
        applyRequest(exam, request);
        return toExamResponse(examRepository.save(exam));
    }

    @Transactional(readOnly = true)
    public Page<ExamResponse> getAllExams(Pageable pageable) {
        return examRepository.findAll(pageable).map(this::toExamResponse);
    }

    @Transactional(readOnly = true)
    public Page<ExamResponse> getActiveExams(Pageable pageable) {
        return examRepository.findByStatus(ExamStatus.ACTIVE, pageable).map(this::toExamResponse);
    }

    @Transactional(readOnly = true)
    public ExamResponse getExamById(Long id) {
        return toExamResponse(findExamOrThrow(id));
    }

    @Transactional
    public ExamResponse updateExam(Long id, ExamRequest request) {
        Exam exam = findExamOrThrow(id);
        applyRequest(exam, request);
        return toExamResponse(examRepository.save(exam));
    }

    @Transactional
    public void deleteExam(Long id) {
        findExamOrThrow(id);
        examRepository.deleteById(id);
    }

    @Transactional
    public ExamResponse updateExamStatus(Long id, ExamStatusRequest request) {
        Exam exam = findExamOrThrow(id);
        exam.setStatus(request.getStatus());
        return toExamResponse(examRepository.save(exam));
    }

    // ── Exam-Question operations ──────────────────────────────────────────────

    @Transactional
    public ExamQuestionResponse assignQuestion(Long examId, AssignQuestionRequest request) {
        Exam exam = findExamOrThrow(examId);
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + request.getQuestionId()));

        if (examQuestionRepository.existsByExamIdAndQuestionId(examId, request.getQuestionId())) {
            throw new BadRequestException("Question is already assigned to this exam");
        }

        int order = request.getQuestionOrder() != null
                ? request.getQuestionOrder()
                : examQuestionRepository.findMaxOrderByExamId(examId) + 1;

        ExamQuestion eq = new ExamQuestion();
        eq.setExam(exam);
        eq.setQuestion(question);
        eq.setQuestionOrder(order);
        eq.setMarksOverride(request.getMarksOverride());

        return toExamQuestionResponse(examQuestionRepository.save(eq));
    }

    @Transactional
    public void removeQuestion(Long examId, Long questionId) {
        findExamOrThrow(examId);
        if (!examQuestionRepository.existsByExamIdAndQuestionId(examId, questionId)) {
            throw new ResourceNotFoundException("Question is not assigned to this exam");
        }
        examQuestionRepository.deleteByExamIdAndQuestionId(examId, questionId);
    }

    @Transactional(readOnly = true)
    public Page<ExamQuestionResponse> getExamQuestions(Long examId, Pageable pageable) {
        findExamOrThrow(examId);
        return examQuestionRepository.findByExamId(examId, pageable).map(this::toExamQuestionResponse);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(Exam exam, ExamRequest request) {
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setInstructions(request.getInstructions());
        exam.setDuration(request.getDuration());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());
        exam.setStartDate(request.getStartDate());
        exam.setEndDate(request.getEndDate());
        exam.setMaxAttempts(request.getMaxAttempts() != null ? request.getMaxAttempts() : 1);
        exam.setShuffleQuestions(request.isShuffleQuestions());
        exam.setShuffleOptions(request.isShuffleOptions());
        exam.setNegativeMarking(request.isNegativeMarking());
        exam.setStatus(request.getStatus() != null ? request.getStatus() : ExamStatus.DRAFT);

        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId()));
            exam.setSubject(subject);
        }

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
            exam.setCourse(course);
        }
    }

    private Exam findExamOrThrow(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));
    }

    private ExamResponse toExamResponse(Exam e) {
        ExamResponse r = new ExamResponse();
        r.setId(e.getId());
        r.setTitle(e.getTitle());
        r.setDescription(e.getDescription());
        r.setInstructions(e.getInstructions());
        r.setDuration(e.getDuration());
        r.setTotalMarks(e.getTotalMarks());
        r.setPassingMarks(e.getPassingMarks());
        r.setStartDate(e.getStartDate());
        r.setEndDate(e.getEndDate());
        r.setMaxAttempts(e.getMaxAttempts());
        r.setShuffleQuestions(e.isShuffleQuestions());
        r.setShuffleOptions(e.isShuffleOptions());
        r.setNegativeMarking(e.isNegativeMarking());
        r.setStatus(e.getStatus());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());

        if (e.getSubject() != null) {
            r.setSubjectId(e.getSubject().getId());
            r.setSubjectName(e.getSubject().getName());
        }
        if (e.getCourse() != null) {
            r.setCourseId(e.getCourse().getId());
            r.setCourseName(e.getCourse().getName());
        }

        r.setQuestionCount((int) examQuestionRepository.countByExamId(e.getId()));
        return r;
    }

    private ExamQuestionResponse toExamQuestionResponse(ExamQuestion eq) {
        ExamQuestionResponse r = new ExamQuestionResponse();
        r.setId(eq.getId());
        r.setQuestionOrder(eq.getQuestionOrder());
        r.setMarksOverride(eq.getMarksOverride());
        r.setQuestion(questionService.toQuestionResponse(eq.getQuestion()));
        return r;
    }
}
