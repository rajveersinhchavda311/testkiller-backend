package com.testkiller.backend.service;

import com.testkiller.backend.dto.question.BulkQuestionRequest;
import com.testkiller.backend.dto.question.QuestionOptionRequest;
import com.testkiller.backend.dto.question.QuestionOptionResponse;
import com.testkiller.backend.dto.question.QuestionRequest;
import com.testkiller.backend.dto.question.QuestionResponse;
import com.testkiller.backend.entity.Question;
import com.testkiller.backend.entity.QuestionOption;
import com.testkiller.backend.entity.Subject;
import com.testkiller.backend.exception.BadRequestException;
import com.testkiller.backend.exception.ResourceNotFoundException;
import com.testkiller.backend.repository.ExamQuestionRepository;
import com.testkiller.backend.repository.QuestionRepository;
import com.testkiller.backend.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final ExamQuestionRepository examQuestionRepository;

    public QuestionService(QuestionRepository questionRepository,
                           SubjectRepository subjectRepository,
                           ExamQuestionRepository examQuestionRepository) {
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.examQuestionRepository = examQuestionRepository;
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        Question question = buildQuestion(request);
        return toQuestionResponse(questionRepository.save(question));
    }

    @Transactional
    public List<QuestionResponse> createQuestionsBulk(BulkQuestionRequest request) {
        return request.getQuestions().stream()
                .map(req -> toQuestionResponse(questionRepository.save(buildQuestion(req))))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<QuestionResponse> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable).map(this::toQuestionResponse);
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long id) {
        return toQuestionResponse(findOrThrow(id));
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = findOrThrow(id);
        applyRequest(question, request);
        // replace all options
        question.getOptions().clear();
        addOptions(question, request.getOptions());
        return toQuestionResponse(questionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        findOrThrow(id);
        if (examQuestionRepository.existsByQuestionId(id)) {
            throw new BadRequestException("Cannot delete: question is assigned to one or more exams");
        }
        questionRepository.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Question buildQuestion(QuestionRequest request) {
        Question question = new Question();
        applyRequest(question, request);
        addOptions(question, request.getOptions());
        return question;
    }

    private void applyRequest(Question question, QuestionRequest request) {
        question.setQuestionText(request.getQuestionText());
        question.setQuestionImageUrl(request.getQuestionImageUrl());
        question.setQuestionType(request.getQuestionType());
        question.setTopic(request.getTopic());
        question.setDifficulty(request.getDifficulty());
        question.setMarks(request.getMarks());
        question.setNegativeMarks(request.getNegativeMarks() != null ? request.getNegativeMarks() : 0.0);
        question.setExplanation(request.getExplanation());
        question.setTags(request.getTags());
        question.setTimeLimit(request.getTimeLimit());
        question.setCorrectAnswer(request.getCorrectAnswer());

        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId()));
            question.setSubject(subject);
        } else {
            question.setSubject(null);
        }
    }

    private void addOptions(Question question, List<QuestionOptionRequest> optionRequests) {
        if (optionRequests == null) return;
        for (int i = 0; i < optionRequests.size(); i++) {
            QuestionOptionRequest req = optionRequests.get(i);
            QuestionOption option = new QuestionOption();
            option.setQuestion(question);
            option.setOptionText(req.getOptionText());
            option.setOptionImageUrl(req.getOptionImageUrl());
            option.setCorrect(req.isCorrect());
            option.setOptionOrder(req.getOptionOrder() > 0 ? req.getOptionOrder() : i);
            question.getOptions().add(option);
        }
    }

    private Question findOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
    }

    QuestionResponse toQuestionResponse(Question q) {
        QuestionResponse r = new QuestionResponse();
        r.setId(q.getId());
        r.setQuestionText(q.getQuestionText());
        r.setQuestionImageUrl(q.getQuestionImageUrl());
        r.setQuestionType(q.getQuestionType());
        r.setTopic(q.getTopic());
        r.setDifficulty(q.getDifficulty());
        r.setMarks(q.getMarks());
        r.setNegativeMarks(q.getNegativeMarks());
        r.setExplanation(q.getExplanation());
        r.setTags(q.getTags());
        r.setTimeLimit(q.getTimeLimit());
        r.setCorrectAnswer(q.getCorrectAnswer());
        r.setCreatedAt(q.getCreatedAt());
        r.setUpdatedAt(q.getUpdatedAt());

        if (q.getSubject() != null) {
            r.setSubjectId(q.getSubject().getId());
            r.setSubjectName(q.getSubject().getName());
        }

        r.setOptions(q.getOptions().stream().map(o -> {
            QuestionOptionResponse or = new QuestionOptionResponse();
            or.setId(o.getId());
            or.setOptionText(o.getOptionText());
            or.setOptionImageUrl(o.getOptionImageUrl());
            or.setCorrect(o.isCorrect());
            or.setOptionOrder(o.getOptionOrder());
            return or;
        }).collect(Collectors.toList()));

        return r;
    }
}
