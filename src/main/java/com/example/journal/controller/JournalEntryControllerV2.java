package com.example.journal.controller;


import com.example.journal.annotations.LogRequest;
import com.example.journal.annotations.LogResponse;
import com.example.journal.annotations.LogTime;
import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.security.core.context.SecurityContextHolder.*;

@Slf4j
@RestController
@RequestMapping("/v2/journals")
public class JournalEntryControllerV2 {


    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @LogResponse
    @LogTime
    @GetMapping("")
    public List<JournalEntry> getJournals(){
        return journalEntryService.findAll();
    }

    @LogRequest
    @LogResponse
    @LogTime
    @GetMapping("/myjournals")
    public ResponseEntity<List<JournalEntry>> getJournalsByUserName(){
        String userName = Objects.requireNonNull(getContext().getAuthentication()).getName();
        Optional<User> userOpt = userService.getUserByUserName(userName);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            List<JournalEntry> journalEntries = user.getJournalEntries();
            return ResponseEntity.status(HttpStatus.OK).body(journalEntries);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
    }

    @LogRequest
    @LogResponse
    @LogTime
    @PostMapping("")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry){
        String userName = Objects.requireNonNull(getContext().getAuthentication()).getName();
        JournalEntry newEntry = journalEntryService.saveEntry(journalEntry, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEntry);
    }

    @LogRequest
    @LogResponse
    @LogTime
    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable String id) {

        String userName = Objects.requireNonNull(getContext().getAuthentication()).getName();

        JournalEntry entry = journalEntryService.getUserOwnedEntry(id, userName);

        return ResponseEntity.ok(entry);
    }


    @LogRequest
    @LogResponse
    @LogTime
    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String id){

        String userName = Objects.requireNonNull(getContext().getAuthentication()).getName();

        journalEntryService.getUserOwnedEntry(id, userName);

        boolean removed = journalEntryService.deleteById(id, userName);
        if(removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Journal Entry");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal Entry Not Found");
    }

    @LogRequest
    @LogResponse
    @LogTime
    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(
            @PathVariable String id,
            @RequestBody JournalEntry journalEntry){

        String userName = Objects.requireNonNull(getContext().getAuthentication()).getName();

        JournalEntry existingEntry = journalEntryService.getUserOwnedEntry(id, userName);

        existingEntry.setTitle(journalEntry.getTitle());

        if (journalEntry.getContent() != null) {
            existingEntry.setContent(journalEntry.getContent());
        }

        JournalEntry updatedEntry = journalEntryService.saveEntry(existingEntry, userName);

        return ResponseEntity.ok(updatedEntry);
    }



}
