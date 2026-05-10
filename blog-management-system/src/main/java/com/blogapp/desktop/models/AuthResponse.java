package com.blogapp.desktop.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication response from backend
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    private User user;
    
    @JsonProperty("accessToken")
    private String token;
    
    public AuthResponse() {
    }
    
    public AuthResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
