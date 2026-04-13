package com.example.journal.controller;

import com.example.journal.annotations.LogRequest;
import com.example.journal.annotations.LogResponse;
import com.example.journal.annotations.LogTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {

    @LogRequest
    @LogResponse
    @LogTime
    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
