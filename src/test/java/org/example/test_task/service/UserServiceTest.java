package org.example.test_task.service;

import org.example.test_task.dto.UserDTO;
import org.example.test_task.entity.User;
import org.example.test_task.exception.NotFountException;
import org.example.test_task.repository.SubscriptionRepository;
import org.example.test_task.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------- getUser -----------

    @Test
    void getUser_success() {
        Long userId = 1L;
        UserDTO userDTO = UserDTO.builder().id(userId).name("Test").build();
        when(userRepository.findDtoById(userId)).thenReturn(Optional.of(userDTO));

        UserDTO result = userService.getUser(userId);

        assertEquals(userId, result.getId());
        assertEquals("Test", result.getName());
        verify(userRepository).findDtoById(userId);
    }

    @Test
    void getUser_notFound() {
        Long userId = 1L;
        when(userRepository.findDtoById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFountException.class, () -> userService.getUser(userId));
        verify(userRepository).findDtoById(userId);
    }

    // ----------- updateUser -----------

    @Test
    void updateUser_success() {
        Long userId = 1L;

        User userFromDb = new User();
        userFromDb.setId(userId);
        userFromDb.setName("Old Name");

        User userFromNet = new User();
        userFromNet.setName("New Name");

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(userFromDb));

        UserDTO result = userService.updateUser(userId, userFromNet);

        assertEquals(userId, result.getId());
        assertEquals("New Name", result.getName());
        verify(userRepository).getUserById(userId);
    }

    @Test
    void updateUser_notFound() {
        Long userId = 1L;
        User userFromNet = new User();
        userFromNet.setName("New Name");

        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFountException.class, () -> userService.updateUser(userId, userFromNet));
        verify(userRepository).getUserById(userId);
    }

    // ----------- createUser -----------

    @Test
    void createUser_success() {
        User user = new User();
        user.setName("Created User");

        User savedUser = new User();
        savedUser.setId(100L);
        savedUser.setName("Created User");

        when(userRepository.save(user)).thenReturn(savedUser);

        UserDTO result = userService.createUser(user);

        assertEquals(100L, result.getId());
        assertEquals("Created User", result.getName());
        verify(userRepository).save(user);
    }

    // ----------- deleteUser -----------

    @Test
    void deleteUser_success() {
        Long userId = 1L;

        // метод ничего не возвращает, проверим вызовы
        doNothing().when(subscriptionRepository).decrementNumberOfUser(userId);
        doNothing().when(userRepository).deleteAllSubscriptionsByUserId(userId);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(subscriptionRepository).decrementNumberOfUser(userId);
        verify(userRepository).deleteAllSubscriptionsByUserId(userId);
        verify(userRepository).deleteById(userId);
    }
}