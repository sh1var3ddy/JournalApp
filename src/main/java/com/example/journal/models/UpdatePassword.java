package com.example.journal.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassword {
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
