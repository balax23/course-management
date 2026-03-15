package com.example.course_management.service;

import com.example.course_management.entity.Category;
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

    public Category createCategory(Category category) {
        log.info("Creating category with name: {}", category.getName());
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll();
    }
}