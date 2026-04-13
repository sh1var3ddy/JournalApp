package com.example.journal.models;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserApiResponse {
    String status;
    String message;
    public UserApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
