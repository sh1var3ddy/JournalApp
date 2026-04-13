package com.example.journal.controller;

import com.example.journal.annotations.LogRequest;
import com.example.journal.annotations.LogResponse;
import com.example.journal.annotations.LogTime;
import com.example.journal.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journals")
public class JournalEntryController {

    private final Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @LogResponse
    @LogTime
    @GetMapping
    public List<JournalEntry> getJournals(){
        return new ArrayList<>(journalEntries.values());
    }

    @LogRequest
    @LogResponse
    @LogTime
    @PostMapping
    public JournalEntry createJournalEntry(@RequestBody JournalEntry journalEntry){
        JournalEntry newEntry = new JournalEntry();
        newEntry.setId(journalEntry.getId());
        newEntry.setTitle(journalEntry.getTitle());
        newEntry.setContent(journalEntry.getContent());
//        journalEntries.put(Long.valueOf(newEntry.getId()), newEntry);
        return newEntry;
    }

    @LogRequest
    @LogResponse
    @LogTime
    @GetMapping("id/{id}")
    public JournalEntry getJournalById(@PathVariable long id){
        return journalEntries.get(id);
    }


    @LogRequest
    @LogResponse
    @LogTime
    @DeleteMapping("id/{id}")
    public JournalEntry deleteJournalEntry(@PathVariable long id){
        return journalEntries.remove(id);
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntry(@PathVariable long id, @RequestBody JournalEntry journalEntry){
        return journalEntries.put(id, journalEntry);
    }

}
