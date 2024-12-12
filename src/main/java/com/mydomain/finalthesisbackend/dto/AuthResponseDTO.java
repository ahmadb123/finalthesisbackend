package com.mydomain.finalthesisbackend.dto;

public class AuthResponseDTO {
    private String token;
    private String role;
    private String userId;

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public AuthResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public AuthResponseDTO(String token, String role, String userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }


    // Getter and Setter
    public String getToken() {  
        return token;
    }


    public String getUserId() {
        return userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
