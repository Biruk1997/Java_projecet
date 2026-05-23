package com.blogapp.desktop.services;

import com.blogapp.desktop.models.User;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.blogapp.desktop.utils.SessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Authentication Service - Handles login, signup, logout
 * Equivalent to React's authService.jsx
 */
public class AuthService {
    
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * Login user
     */
    public static CompletableFuture<LoginResponse> login(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        
        System.out.println("DEBUG: Attempting login with email: " + email);
        
        return HttpClientUtil.post("/auth/login", body, null, JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Received response: " + response.toString());
                        
                        // Backend returns: { success, message, data: { user, accessToken } }
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in response");
                            throw new RuntimeException("No data in response");
                        }
                        
                        LoginResponse loginResponse = new LoginResponse();
                        loginResponse.token = dataNode.get("accessToken").asText();
                        loginResponse.user = mapper.treeToValue(dataNode.get("user"), User.class);
                        
                        System.out.println("DEBUG: Login successful for user: " + loginResponse.user.getUsername());
                        
                        // Save session
                        SessionManager.saveSession(
                            loginResponse.token,
                            loginResponse.user.getUserId(),
                            loginResponse.user.getUsername(),
                            loginResponse.user.getEmail()
                        );
                        
                        return loginResponse;
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse login response: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to parse login response: " + e.getMessage(), e);
                    }
                })
                .exceptionally(error -> {
                    System.err.println("ERROR: Login request failed: " + error.getMessage());
                    error.printStackTrace();
                    throw new RuntimeException(error);
                });
    }
    
    /**
     * Register new user
     */
    public static CompletableFuture<User> signup(SignupRequest request) {
        Map<String, String> body = new HashMap<>();
        // Backend expects displayName (combination of firstname + lastname)
        String displayName = request.firstname + " " + request.lastname;
        body.put("displayName", displayName);
        body.put("username", request.username);
        body.put("email", request.email);
        body.put("password", request.password);
        if (request.bio != null) {
            body.put("bio", request.bio);
        }
        
        return HttpClientUtil.post("/auth/signup", body, null, JsonNode.class)
                .thenApply(response -> {
                    try {
                        // Backend returns: { success, message, data: { user, accessToken } }
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            throw new RuntimeException("No data in response");
                        }
                        
                        User user = mapper.treeToValue(dataNode.get("user"), User.class);
                        String token = dataNode.get("accessToken").asText();
                        
                        // Save session
                        SessionManager.saveSession(token, user.getUserId(), user.getUsername(), user.getEmail());
                        
                        return user;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse signup response: " + e.getMessage(), e);
                    }
                });
    }
    
    /**
     * Logout (client-side only)
     */
    public static void logout() {
        SessionManager.clearSession();
    }
    
    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated() {
        return SessionManager.getToken() != null;
    }
    
    /**
     * Get current user ID
     */
    public static String getCurrentUserId() {
        return SessionManager.getUserId();
    }
    
    /**
     * Get current token
     */
    public static String getToken() {
        return SessionManager.getToken();
    }
    
    // DTOs
    public static class LoginResponse {
        public String token;
        public User user;
    }
    
    public static class SignupRequest {
        public String firstname;
        public String lastname;
        public String username;
        public String email;
        public String password;
        public String bio;
    }
}
