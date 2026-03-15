package com.example.course_management.service;

import com.example.course_management.dto.CategoryCreateRequest;
import com.example.course_management.dto.CategoryResponse;
import com.example.course_management.entity.Category;
import com.example.course_management.mapper.CategoryMapper;
import com.example.course_management.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryCreateRequest request) {
        log.info("Creating category with name: {}", request.getName());

        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories");

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}