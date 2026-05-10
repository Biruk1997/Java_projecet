package com.blogapp.desktop.services;

import com.blogapp.desktop.models.User;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * User Service - Handles user-related operations
 */
public class UserService {
    
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * Get all users in the system
     */
    public static CompletableFuture<List<User>> getAllUsers(String token) {
        return HttpClientUtil.get("/users", token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Get all users response: " + response.toString());
                        // Backend returns: { success, message, data: [UserDTO] }
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in get users response");
                            return new ArrayList<>();
                        }
                        
                        return mapper.readValue(dataNode.toString(), new TypeReference<List<User>>() {});
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse users: " + e.getMessage());
                        e.printStackTrace();
                        return new ArrayList<>();
                    }
                });
    }
    
    /**
     * Search users by name or username
     */
    public static CompletableFuture<List<User>> searchUsers(String query, String token) {
        return HttpClientUtil.get("/users/search?q=" + query, token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Search users response: " + response.toString());
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in search users response");
                            return new ArrayList<>();
                        }
                        
                        return mapper.readValue(dataNode.toString(), new TypeReference<List<User>>() {});
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse search results: " + e.getMessage());
                        e.printStackTrace();
                        return new ArrayList<>();
                    }
                });
    }
    
    /**
     * Get user by ID
     */
    public static CompletableFuture<User> getUserById(String userId, String token) {
        return HttpClientUtil.get("/users/" + userId, token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            throw new RuntimeException("No data in response");
                        }
                        
                        return mapper.readValue(dataNode.toString(), User.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse user", e);
                    }
                });
    }
}
