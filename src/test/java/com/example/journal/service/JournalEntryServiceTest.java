package com.example.journal.service;


import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.exception.JournalEntryException;
import com.example.journal.repository.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JournalEntryServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @InjectMocks
    private JournalEntryService journalEntryService;

    @Mock
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User("john_doe", "password123");
        testUser.setId("user1");
        testUser.setRole("USER");
        testUser.setJournalEntries(new ArrayList<>());

        // Create test journal entries
        JournalEntry testEntry = new JournalEntry();
        testEntry.setId("entry1");
        testEntry.setTitle("My First Entry");
        testEntry.setContent("This is my first journal entry");
        testEntry.setDate(LocalDateTime.now());

        JournalEntry testEntry2 = new JournalEntry();
        testEntry2.setId("entry2");
        testEntry2.setTitle("My Second Entry");
        testEntry2.setContent("This is my second journal entry");
        testEntry2.setDate(LocalDateTime.now());
    }

    @Test
    void saveEntry_WithValidUSerEntry_ShouldSaveEntry() {
        String userName = "john_doe";
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId("entry1");
        journalEntry.setTitle("My First Entry");
        journalEntry.setContent("This is my first journal entry");
        journalEntry.setDate(LocalDateTime.now());

        JournalEntry journalEntry2 = new JournalEntry();
        journalEntry2.setId("entry2");
        journalEntry2.setTitle("My Second Entry");
        journalEntry2.setContent("This is my second journal entry");
        journalEntry2.setDate(LocalDateTime.now());

        when(userService.getUserByUserName(userName)).thenReturn(Optional.of(testUser));
        when(journalEntryRepository.save(journalEntry)).thenReturn(journalEntry);
        when(userService.updateUser(any(User.class))).thenReturn(testUser);

        // act
        JournalEntry result = journalEntryService.saveEntry(journalEntry, userName);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("My First Entry", result.getTitle());
        assertNotNull(result.getDate()); // Date should be set automatically

        // Verify interactions
        verify(userService, times(1)).getUserByUserName(userName);
        verify(journalEntryRepository, times(1)).save(any(JournalEntry.class));
        verify(userService, times(1)).updateUser(testUser);

    }

    @Test
    void saveEntry_WithInvalidUSerEntry_ShouldThrowException() {
        String userName = "john_doe";
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId("entry1");
        journalEntry.setTitle("My First Entry");
        journalEntry.setContent("This is my first journal entry");
        journalEntry.setDate(LocalDateTime.now());

        when(userService.getUserByUserName(userName)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,()->{
            journalEntryService.saveEntry(journalEntry, userName);
        });

        verify(journalEntryRepository, never()).save(any());
    }

    @Test
    void saveEntry_WhenRepositoryFails_ShouldThrowJournalEntryException() {
        // Arrange
        String userName = "john_doe";
        JournalEntry newEntry = new JournalEntry();

        when(userService.getUserByUserName(userName)).thenReturn(Optional.of(testUser));
        when(journalEntryRepository.save(any(JournalEntry.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(JournalEntryException.class, () -> {
            journalEntryService.saveEntry(newEntry, userName);
        });
    }
}
