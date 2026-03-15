package com.example.course_management.service;

import com.example.course_management.dto.CourseCreateRequest;
import com.example.course_management.dto.CourseResponse;
import com.example.course_management.entity.Category;
import com.example.course_management.entity.Course;
import com.example.course_management.entity.User;
import com.example.course_management.exception.ResourceNotFoundException;
import com.example.course_management.mapper.CourseMapper;
import com.example.course_management.repository.CategoryRepository;
import com.example.course_management.repository.CourseRepository;
import com.example.course_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;

    public CourseResponse createCourse(CourseCreateRequest request) {
        log.info("Creating course with title: {}", request.getTitle());

        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + request.getInstructorId()));

        Set<Category> categories = getCategoriesByIds(request.getCategoryIds());
        Set<User> students = getUsersByIds(request.getStudentIds());

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .durationInHours(request.getDurationInHours())
                .instructor(instructor)
                .categories(categories)
                .students(students)
                .build();

        Course savedCourse = courseRepository.save(course);
        log.info("Course created successfully with id: {}", savedCourse.getId());

        return courseMapper.toResponse(savedCourse);
    }

    public java.util.List<CourseResponse> getAllCourses() {
        log.info("Fetching all courses");

        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public CourseResponse getCourseById(Long id) {
        log.info("Fetching course by id: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        return courseMapper.toResponse(course);
    }

    public CourseResponse updateCourse(Long id, CourseCreateRequest request) {
        log.info("Updating course with id: {}", id);

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + request.getInstructorId()));

        Set<Category> categories = getCategoriesByIds(request.getCategoryIds());
        Set<User> students = getUsersByIds(request.getStudentIds());

        existingCourse.setTitle(request.getTitle());
        existingCourse.setDescription(request.getDescription());
        existingCourse.setDurationInHours(request.getDurationInHours());
        existingCourse.setInstructor(instructor);
        existingCourse.setCategories(categories);
        existingCourse.setStudents(students);

        Course updatedCourse = courseRepository.save(existingCourse);
        log.info("Course updated successfully with id: {}", updatedCourse.getId());

        return courseMapper.toResponse(updatedCourse);
    }

    public void deleteCourse(Long id) {
        log.info("Deleting course with id: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        courseRepository.delete(course);
        log.info("Course deleted successfully with id: {}", id);
    }

    private Set<Category> getCategoriesByIds(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Collections.emptySet();
        }

        return categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId)))
                .collect(Collectors.toSet());
    }

    private Set<User> getUsersByIds(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptySet();
        }

        return userIds.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId)))
                .collect(Collectors.toSet());
    }
    public CourseResponse enrollStudent(Long courseId, Long studentId) {
        log.info("Enrolling student {} to course {}", studentId, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + studentId));

        course.getStudents().add(student);

        Course updatedCourse = courseRepository.save(course);
        log.info("Student {} enrolled successfully to course {}", studentId, courseId);

        return courseMapper.toResponse(updatedCourse);
    }
}