package com.example.course_management.mapper;

import com.example.course_management.dto.CategoryResponse;
import com.example.course_management.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}