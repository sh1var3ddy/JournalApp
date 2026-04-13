package com.example.journal.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.journal.entity.JournalEntry;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    @NotBlank(message = "userName is required")
    private String userName;

    @NonNull
    @NotBlank(message = "password is required")
    private String password;

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();

    private String role;
}
