package com.example.journal.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GetUserResponse {
    private String userName;
    private String id;
    private String role;
}
