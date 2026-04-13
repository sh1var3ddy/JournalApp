package com.example.journal.controller;

import com.example.journal.annotations.LogRequest;
import com.example.journal.annotations.LogResponse;
import com.example.journal.annotations.LogTime;
import com.example.journal.entity.User;
import com.example.journal.exception.UserAlreadyExistsException;
import com.example.journal.models.*;
import com.example.journal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @LogResponse
    @LogRequest
    @LogTime
    @GetMapping("/user")
    public ResponseEntity<GetUserResponse> getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        GetUserResponse response = GetUserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .role(user.getRole())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @LogTime
    @PutMapping("/user")
    public ResponseEntity<UserApiResponse> updateUser(String newUserName) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(newUserName.isEmpty()) {
            throw new Exception("Username should not be empty");
        }
        Optional<User> existingUserOpt = this.userService.getUserByUserName(username);
        Optional<User> newUserOpt = this.userService.getUserByUserName(newUserName);

        if(newUserOpt.isPresent()){
            throw new UserAlreadyExistsException(
                    "User with username '" + newUserName + "' already exists"
            );
        }

        User existingUser = existingUserOpt.get();

        if (newUserName.trim().isEmpty() || username.equals(newUserName.trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserApiResponse("error", "Username cannot be null or same as existing one"));
        }

        // Update username
        existingUser.setUserName(newUserName.trim());
        this.userService.updateUser(existingUser);

        return ResponseEntity.status(HttpStatus.OK).body(new UserApiResponse("success", "User updated successfully"));
    }

    @LogRequest
    @LogResponse
    @LogTime
    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserApiResponse> deleteUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(new UserApiResponse("success", "User created successfully"));
    }

}
