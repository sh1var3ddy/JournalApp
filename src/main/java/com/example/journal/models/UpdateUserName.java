package com.example.journal.models;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateUserName {
    @NotBlank(message = "New username is required")
    private String newUserName;

    @NotBlank(message = "Existing username is required")
    private String existingUserName;

    @NotBlank(message = "Password is required to update username")
    private String password;
}