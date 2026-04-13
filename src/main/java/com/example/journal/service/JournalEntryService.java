package com.example.journal.service;

import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.exception.JournalEntryException;
import com.example.journal.repository.JournalEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;
    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String userName) {
        User user = userService.getUserByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        try {
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry newEntry = this.journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(newEntry);
            userService.updateUser(user);
            return newEntry;
        } catch (Exception e) {
            throw new JournalEntryException("Failed to save journal entry: " + e.getMessage());
        }
    }
    @Transactional(readOnly = true)
    public List<JournalEntry> findAll() {
        return this.journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(String id) {
        return this.journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(String id, String userName) {
        try {
            User user = userService.getUserByUserName(userName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
            boolean removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
            if(removed) {
                userService.updateUser(user);
                this.journalEntryRepository.deleteById(id);
                return true;
            }
            return removed;
        }
        catch (Exception e) {
            throw new JournalEntryException("Failed to delete journal entry: " + e.getMessage());
        }
    }

    public JournalEntry updateJournalEntry(JournalEntry journalEntry) {
        return this.journalEntryRepository.save(journalEntry);
    }

    public JournalEntry getUserOwnedEntry(String id, String userName) {

        User user = userService.getUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));

        boolean isOwner = user.getJournalEntries()
                .stream()
                .anyMatch(e -> e.getId().equals(id));

        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return entry;
    }

}
