package com.example.journal.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// CAn use @Data instead of all these annotations
@Document(collection = "journal_entries")
public class JournalEntry {

    @Id
    private String id;

    @NonNull
    private String title;

    private String content;

    private LocalDateTime date;
}
