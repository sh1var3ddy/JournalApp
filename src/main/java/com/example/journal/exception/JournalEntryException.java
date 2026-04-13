package com.example.journal.exception;

public class JournalEntryException extends RuntimeException {
    public JournalEntryException(String message) {
        super(message);
    }
}