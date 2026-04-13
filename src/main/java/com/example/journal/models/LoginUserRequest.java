package com.example.journal.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LoginUserRequest {
    @NonNull
    @NotBlank(message = "userName is required")
    private String userName;

    @NonNull
    @NotBlank(message = "password is required")
    private String password;

}
