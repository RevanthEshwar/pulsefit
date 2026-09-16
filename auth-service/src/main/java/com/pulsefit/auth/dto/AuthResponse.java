package com.pulsefit.auth.dto;

public class AuthResponse {

    private String message;
    private String username;
    private String email;
    private String role;

    public AuthResponse() {
    }

    public AuthResponse(String message, String username, String email, String role) {
        this.message = message;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}