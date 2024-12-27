package com.example.dotcode.service.impl;

import com.example.dotcode.entity.User;
import com.example.dotcode.entity.dto.UserDto;
import com.example.dotcode.entity.mapper.UserMapper;
import com.example.dotcode.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User user1 = new User(1, "John", "Doe", "john.doe@example.com");
        User user2 = new User(2, "Jane", "Doe", "jane.doe@example.com");
        UserDto userDto1 = new UserDto(1, "John", "Doe", "john.doe@example.com");
        UserDto userDto2 = new UserDto(2, "Jane", "Doe", "jane.doe@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(userDto1);
        when(userMapper.toDto(user2)).thenReturn(userDto2);
    }

    @Test
    void testGetAll() {
        List<UserDto> result = userService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Jane", result.get(1).getName());
    }

    @Test
    void testSave() {
        UserDto userDto = new UserDto(1, "John", "Doe", "john.doe@example.com");
        User user = new User(1, "John", "Doe", "john.doe@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.save(userDto);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testUpdateExistingUser() {
        UserDto userDto = new UserDto(1, "John", "Doe", "john.doe@example.com");
        User user = new User(1, "John", "Doe", "john.doe@example.com");

        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.update(1, userDto);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testUpdateNonExistingUser() {
        UserDto userDto = new UserDto(1, "John", "Doe", "john.doe@example.com");

        when(userRepository.existsById(1)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(1, userDto);
        });

        assertEquals("User with ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).deleteById(1);

        Integer result = userService.delete(1);

        verify(userRepository, times(1)).deleteById(1);

        assertEquals(1, result);
    }
}
