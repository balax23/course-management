package com.example.course_management;

import com.example.course_management.entity.Category;
import com.example.course_management.entity.User;
import com.example.course_management.repository.CategoryRepository;
import com.example.course_management.repository.CourseRepository;
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

    @Autowired
    private CourseRepository courseRepository;

    private Long instructorId;
    private Long studentId;
    private Long categoryId;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
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

        instructor = userRepository.save(instructor);
        student = userRepository.save(student);

        Category category = Category.builder()
                .name("Programming")
                .build();

        category = categoryRepository.save(category);

        instructorId = instructor.getId();
        studentId = student.getId();
        categoryId = category.getId();
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
                  "instructorId": %d,
                  "categoryIds": [%d],
                  "studentIds": [%d]
                }
                """.formatted(instructorId, categoryId, studentId);

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
                  "instructorId": %d,
                  "categoryIds": [%d],
                  "studentIds": [%d]
                }
                """.formatted(instructorId, categoryId, studentId);

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
                  "instructorId": %d,
                  "categoryIds": [%d],
                  "studentIds": [%d]
                }
                """.formatted(instructorId, categoryId, studentId);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Long createdCourseId = courseRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/courses/" + createdCourseId))
                .andExpect(status().isOk());
    }
}