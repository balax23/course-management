package com.example.course_management;

import com.example.course_management.dto.UserCreateRequest;
import com.example.course_management.dto.UserResponse;
import com.example.course_management.entity.User;
import com.example.course_management.mapper.UserMapper;
import com.example.course_management.repository.UserRepository;
import com.example.course_management.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldReturnUserResponse() {
        UserCreateRequest request = new UserCreateRequest();
        request.setFullName("John Doe");
        request.setEmail("john@test.com");

        User savedUser = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(response);

        UserResponse result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@test.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers_ShouldReturnUserResponseList() {
        User user = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        verify(userRepository, times(1)).findAll();
    }
}