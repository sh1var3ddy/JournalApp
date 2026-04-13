package com.example.journal.service;

import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        // Option 1: Use RequiredArgsConstructor from Lombok
        testUser1 = createUser("1", "john_doe", "password123", "USER");
        testUser2 = createUser("2", "jane_smith", "password456", "USER");
    }

    // Helper method to create users properly
    private User createUser(String id, String userName, String password, String role) {
        User user = new User("dummyUserName", "dummyPassword"); // @NonNull constructor
        user.setId(id);
        user.setUserName(userName);
        user.setPassword(password);
        user.setRole(role);
        user.setJournalEntries(new ArrayList<>());
        return user;
    }

    // ==================== getAllUsers Tests ====================

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(testUser1, testUser2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals("john_doe", actualUsers.get(0).getUserName());
        assertEquals("jane_smith", actualUsers.get(1).getUserName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertNotNull(actualUsers);
        assertTrue(actualUsers.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    // ==================== getUserById Tests ====================

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        String userId = "1";
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser1));

        // Act
        Optional<User> result = userService.getUserById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals("john_doe", result.get().getUserName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        String userId = "999";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    // ==================== getUserByUserName Tests ====================

    @Test
    void getUserByUserName_WhenUserExists_ShouldReturnUser() {
        // Arrange
        String userName = "john_doe";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(testUser1));

        // Act
        Optional<User> result = userService.getUserByUserName(userName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("john_doe", result.get().getUserName());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    void getUserByUserName_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        String userName = "nonexistent_user";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserByUserName(userName);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    // ==================== updateUser Tests ====================

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Arrange
        User userToUpdate = createUser("1", "john_doe_updated", "newPassword123", "ADMIN");

        // Mock to return what we pass in
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUser(userToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("john_doe_updated", result.getUserName());
        assertEquals("newPassword123", result.getPassword());
        assertEquals("ADMIN", result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldCallRepositorySave() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser1);

        // Act
        User result = userService.updateUser(testUser1);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(testUser1);
    }

    @Test
    void updateUser_WithModifiedPassword_ShouldUpdateSuccessfully() {
        // Arrange
        User userWithNewPassword = createUser("1", "john_doe", "newEncryptedPassword", "USER");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUser(userWithNewPassword);

        // Assert
        assertNotNull(result);
        assertEquals("newEncryptedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ==================== deleteUser Tests ====================

    @Test
    void deleteUser_ShouldReturnSuccessMessage() {
        // Arrange
        String userId = "1";
        doNothing().when(userRepository).deleteById(userId);

        // Act
        String result = userService.deleteUser(userId);

        // Assert
        assertEquals("User deleted", result);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldCallRepositoryDeleteById() {
        // Arrange
        String userId = "1";

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}