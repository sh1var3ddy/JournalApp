package com.example.journal.models;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserResponse {
    private String status;
    private String message;
    private String token;
}
