package com.example.course_management.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Integer durationInHours;
    private String instructorName;
    private String instructorEmail;
    private Set<String> categories;
    private Set<String> students;
}