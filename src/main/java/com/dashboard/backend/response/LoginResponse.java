package com.dashboard.backend.response;

public class LoginResponse {
    private String token;
    private String email;

    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    // Getters en Setters
}
