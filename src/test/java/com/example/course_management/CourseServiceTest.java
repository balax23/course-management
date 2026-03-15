package com.example.course_management;

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
import com.example.course_management.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_ShouldReturnCourseResponse() {
        User instructor = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        User student = User.builder()
                .id(2L)
                .fullName("Peter Smith")
                .email("peter@test.com")
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("Programming")
                .build();

        CourseCreateRequest request = new CourseCreateRequest();
        request.setTitle("Spring Boot Basics");
        request.setDescription("Intro course");
        request.setDurationInHours(20);
        request.setInstructorId(1L);
        request.setCategoryIds(Set.of(1L));
        request.setStudentIds(Set.of(2L));

        Course savedCourse = Course.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructor(instructor)
                .categories(Set.of(category))
                .students(Set.of(student))
                .build();

        CourseResponse response = CourseResponse.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructorName("John Doe")
                .instructorEmail("john@test.com")
                .categories(Set.of("Programming"))
                .students(Set.of("Peter Smith"))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);
        when(courseMapper.toResponse(savedCourse)).thenReturn(response);

        CourseResponse result = courseService.createCourse(request);

        assertNotNull(result);
        assertEquals("Spring Boot Basics", result.getTitle());
        assertEquals("John Doe", result.getInstructorName());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void createCourse_ShouldThrowException_WhenInstructorNotFound() {
        CourseCreateRequest request = new CourseCreateRequest();
        request.setTitle("Spring Boot Basics");
        request.setDescription("Intro course");
        request.setDurationInHours(20);
        request.setInstructorId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.createCourse(request)
        );

        assertEquals("Instructor not found with id: 1", exception.getMessage());
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void getAllCourses_ShouldReturnCourseResponseList() {
        User instructor = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        Course course = Course.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructor(instructor)
                .categories(new HashSet<>())
                .students(new HashSet<>())
                .build();

        CourseResponse response = CourseResponse.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructorName("John Doe")
                .instructorEmail("john@test.com")
                .categories(Set.of())
                .students(Set.of())
                .build();

        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(courseMapper.toResponse(course)).thenReturn(response);

        List<CourseResponse> result = courseService.getAllCourses();

        assertEquals(1, result.size());
        assertEquals("Spring Boot Basics", result.get(0).getTitle());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_ShouldReturnCourseResponse() {
        User instructor = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        Course course = Course.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructor(instructor)
                .categories(new HashSet<>())
                .students(new HashSet<>())
                .build();

        CourseResponse response = CourseResponse.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructorName("John Doe")
                .instructorEmail("john@test.com")
                .categories(Set.of())
                .students(Set.of())
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(response);

        CourseResponse result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void deleteCourse_ShouldDeleteCourse() {
        User instructor = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        Course course = Course.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructor(instructor)
                .categories(new HashSet<>())
                .students(new HashSet<>())
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void enrollStudent_ShouldAddStudentToCourse() {
        User instructor = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        User student = User.builder()
                .id(2L)
                .fullName("Peter Smith")
                .email("peter@test.com")
                .build();

        Course course = Course.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructor(instructor)
                .categories(new HashSet<>())
                .students(new HashSet<>())
                .build();

        Course savedCourse = Course.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructor(instructor)
                .categories(new HashSet<>())
                .students(new HashSet<>(Set.of(student)))
                .build();

        CourseResponse response = CourseResponse.builder()
                .id(1L)
                .title("Spring Boot Basics")
                .description("Intro course")
                .durationInHours(20)
                .instructorName("John Doe")
                .instructorEmail("john@test.com")
                .categories(Set.of())
                .students(Set.of("Peter Smith"))
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);
        when(courseMapper.toResponse(savedCourse)).thenReturn(response);

        CourseResponse result = courseService.enrollStudent(1L, 2L);

        assertNotNull(result);
        assertEquals("Spring Boot Basics", result.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }
}