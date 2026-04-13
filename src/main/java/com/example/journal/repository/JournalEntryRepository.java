package com.example.journal.repository;

import com.example.journal.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
}
