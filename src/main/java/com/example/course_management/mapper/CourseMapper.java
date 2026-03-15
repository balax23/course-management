package com.example.course_management.mapper;

import com.example.course_management.dto.CourseResponse;
import com.example.course_management.entity.Course;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CourseMapper {

    public CourseResponse toResponse(Course course) {
        Set<String> categoryNames = course.getCategories()
                .stream()
                .map(category -> category.getName())
                .collect(java.util.stream.Collectors.toSet());

        Set<String> studentNames = course.getStudents()
                .stream()
                .map(student -> student.getFullName())
                .collect(java.util.stream.Collectors.toSet());

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .durationInHours(course.getDurationInHours())
                .instructorName(course.getInstructor().getFullName())
                .instructorEmail(course.getInstructor().getEmail())
                .categories(categoryNames)
                .students(studentNames)
                .build();
    }
}