package com.testkiller.backend.service;

import com.testkiller.backend.dto.course.CourseRequest;
import com.testkiller.backend.dto.course.CourseResponse;
import com.testkiller.backend.dto.course.SubjectRequest;
import com.testkiller.backend.dto.course.SubjectResponse;
import com.testkiller.backend.entity.Course;
import com.testkiller.backend.entity.Subject;
import com.testkiller.backend.exception.BadRequestException;
import com.testkiller.backend.exception.ResourceNotFoundException;
import com.testkiller.backend.repository.CourseRepository;
import com.testkiller.backend.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    public CourseService(CourseRepository courseRepository, SubjectRepository subjectRepository) {
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
    }

    // ── Course operations ─────────────────────────────────────────────────────

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByName(request.getName())) {
            throw new BadRequestException("Course with name '" + request.getName() + "' already exists");
        }
        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        course.setActive(request.isActive());
        return toCourseResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::toCourseResponse);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        return toCourseResponse(findCourseOrThrow(id));
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = findCourseOrThrow(id);
        if (!course.getName().equals(request.getName()) && courseRepository.existsByName(request.getName())) {
            throw new BadRequestException("Course with name '" + request.getName() + "' already exists");
        }
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        course.setActive(request.isActive());
        return toCourseResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        findCourseOrThrow(id);
        if (subjectRepository.existsByCourseId(id)) {
            throw new BadRequestException("Cannot delete course: it still has subjects. Remove subjects first.");
        }
        courseRepository.deleteById(id);
    }

    // ── Subject operations ────────────────────────────────────────────────────

    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        Course course = findCourseOrThrow(request.getCourseId());
        if (subjectRepository.existsByNameAndCourseId(request.getName(), request.getCourseId())) {
            throw new BadRequestException("Subject '" + request.getName() + "' already exists in this course");
        }
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        subject.setCourse(course);
        return toSubjectResponse(subjectRepository.save(subject));
    }

    @Transactional(readOnly = true)
    public Page<SubjectResponse> getAllSubjects(Long courseId, Pageable pageable) {
        if (courseId != null) {
            return subjectRepository.findByCourseId(courseId, pageable).map(this::toSubjectResponse);
        }
        return subjectRepository.findAll(pageable).map(this::toSubjectResponse);
    }

    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long id) {
        return toSubjectResponse(findSubjectOrThrow(id));
    }

    @Transactional
    public SubjectResponse updateSubject(Long id, SubjectRequest request) {
        Subject subject = findSubjectOrThrow(id);
        Course course = findCourseOrThrow(request.getCourseId());

        boolean nameChanged = !subject.getName().equals(request.getName());
        boolean courseChanged = !subject.getCourse().getId().equals(request.getCourseId());
        if ((nameChanged || courseChanged)
                && subjectRepository.existsByNameAndCourseId(request.getName(), request.getCourseId())) {
            throw new BadRequestException("Subject '" + request.getName() + "' already exists in this course");
        }

        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        subject.setCourse(course);
        return toSubjectResponse(subjectRepository.save(subject));
    }

    @Transactional
    public void deleteSubject(Long id) {
        findSubjectOrThrow(id);
        subjectRepository.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private Subject findSubjectOrThrow(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
    }

    private CourseResponse toCourseResponse(Course c) {
        CourseResponse r = new CourseResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setDescription(c.getDescription());
        r.setDuration(c.getDuration());
        r.setActive(c.isActive());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }

    private SubjectResponse toSubjectResponse(Subject s) {
        SubjectResponse r = new SubjectResponse();
        r.setId(s.getId());
        r.setName(s.getName());
        r.setDescription(s.getDescription());
        r.setCourseId(s.getCourse().getId());
        r.setCourseName(s.getCourse().getName());
        r.setCreatedAt(s.getCreatedAt());
        r.setUpdatedAt(s.getUpdatedAt());
        return r;
    }
}
