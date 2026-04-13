package com.example.journal.utils;


import com.example.journal.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class UserValidator {

    private final BCryptPasswordEncoder passwordEncoder;

    public UserValidator(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isValidUser(User existingUser, String password) {
        return passwordEncoder.matches(password, existingUser.getPassword());
    }
}
