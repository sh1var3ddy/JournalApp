package com.example.journal.service;


import com.example.journal.entity.User;
import com.example.journal.exception.UserAlreadyExistsException;
import com.example.journal.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        user.setPassword(Objects.requireNonNull(passwordEncoder.encode(user.getPassword())));
        return userRepository.save(user);
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

}
