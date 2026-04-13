package com.example.journal.controller;

import com.example.journal.annotations.LogTime;
import com.example.journal.entity.User;
import com.example.journal.models.LoginUserRequest;
import com.example.journal.models.LoginUserResponse;
import com.example.journal.models.UserApiResponse;
import com.example.journal.service.AuthService;
import com.example.journal.service.JwtService;
import com.example.journal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private  UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;


    @LogTime
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@Valid @RequestBody LoginUserRequest request) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );

            Optional<User> optionalUser = userService.getUserByUserName(request.getUserName());
            String token = jwtService.generateToken(optionalUser.get().getUserName());

            return ResponseEntity.ok(
                    new LoginUserResponse("success", "login successful", token)
            );

        }catch(BadCredentialsException e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginUserResponse("error","Invalid username or password",null));
        }
    }

    @LogTime
    @PostMapping("/register")
    public ResponseEntity<UserApiResponse> registerUser(@Valid @RequestBody User user) {
        User savedUser = authService.registerUser(user);

        URI location = URI.create("/register/" + savedUser.getId()); // endpoint for created user

        return ResponseEntity
                .created(location) // sets 201 + Location header
                .body(new UserApiResponse("success", "User created successfully"));
    }

}
