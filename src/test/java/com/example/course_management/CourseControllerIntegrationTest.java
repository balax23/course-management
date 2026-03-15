package com.example.course_management;

import com.example.course_management.entity.Category;
import com.example.course_management.entity.User;
import com.example.course_management.repository.CategoryRepository;
import com.example.course_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        User instructor = User.builder()
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        User student = User.builder()
                .fullName("Peter Smith")
                .email("peter@test.com")
                .build();

        userRepository.save(instructor);
        userRepository.save(student);

        Category category = Category.builder()
                .name("Programming")
                .build();

        categoryRepository.save(category);
    }

    @Test
    void getAllCourses_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk());
    }

    @Test
    void createCourse_ShouldReturnCreatedCourse() throws Exception {
        String requestBody = """
                {
                  "title": "Spring Boot Basics",
                  "description": "Intro course",
                  "durationInHours": 20,
                  "instructorId": 1,
                  "categoryIds": [1],
                  "studentIds": [2]
                }
                """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot Basics"))
                .andExpect(jsonPath("$.instructorName").value("John Doe"));
    }

    @Test
    void createCourse_ShouldReturnBadRequest_WhenTitleIsBlank() throws Exception {
        String requestBody = """
                {
                  "title": "",
                  "description": "Intro course",
                  "durationInHours": 20,
                  "instructorId": 1,
                  "categoryIds": [1],
                  "studentIds": [2]
                }
                """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCourse_ShouldReturnOk() throws Exception {
        String requestBody = """
                {
                  "title": "Spring Boot Basics",
                  "description": "Intro course",
                  "durationInHours": 20,
                  "instructorId": 1,
                  "categoryIds": [1],
                  "studentIds": [2]
                }
                """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isOk());
    }
}