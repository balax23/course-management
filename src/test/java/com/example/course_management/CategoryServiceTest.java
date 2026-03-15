package com.example.course_management;

import com.example.course_management.dto.CategoryCreateRequest;
import com.example.course_management.dto.CategoryResponse;
import com.example.course_management.entity.Category;
import com.example.course_management.mapper.CategoryMapper;
import com.example.course_management.repository.CategoryRepository;
import com.example.course_management.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_ShouldReturnCategoryResponse() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Programming");

        Category savedCategory = Category.builder()
                .id(1L)
                .name("Programming")
                .build();

        CategoryResponse response = CategoryResponse.builder()
                .id(1L)
                .name("Programming")
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toResponse(savedCategory)).thenReturn(response);

        CategoryResponse result = categoryService.createCategory(request);

        assertNotNull(result);
        assertEquals("Programming", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getAllCategories_ShouldReturnCategoryResponseList() {
        Category category = Category.builder()
                .id(1L)
                .name("Programming")
                .build();

        CategoryResponse response = CategoryResponse.builder()
                .id(1L)
                .name("Programming")
                .build();

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(response);

        List<CategoryResponse> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }
}