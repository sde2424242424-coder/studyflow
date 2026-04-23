package com.example.studyflow.network.responses;

public class LoginResponseDto {

    private String token;
    private UserResponseDto user;

    public String getToken() {
        return token;
    }

    public UserResponseDto getUser() {
        return user;
    }
}