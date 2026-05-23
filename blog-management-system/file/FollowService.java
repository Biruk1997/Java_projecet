package com.blogapp.desktop.services;

import com.blogapp.desktop.models.User;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Follow Service - Handles follow/unfollow operations
 * Equivalent to React's followService.jsx
 */
public class FollowService {
    
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * Follow a user
     */
    public static CompletableFuture<Void> followUser(String userId, String token) {
        Map<String, String> body = new HashMap<>();
        body.put("userId", userId);
        
        return HttpClientUtil.post("/follow/" + userId, body, token, JsonNode.class)
                .thenApply(response -> {
                    System.out.println("DEBUG: Follow user response: " + response.toString());
                    return null;
                });
    }
    
    /**
     * Unfollow a user
     */
    public static CompletableFuture<Void> unfollowUser(String userId, String token) {
        return HttpClientUtil.delete("/follow/" + userId, token)
                .thenApply(v -> null);
    }
    
    /**
     * Get user's followers
     */
    public static CompletableFuture<List<User>> getFollowers(String userId, String token) {
        return HttpClientUtil.get("/follow/" + userId + "/followers", token, com.fasterxml.jackson.databind.JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Get followers response: " + response.toString());
                        // Backend returns: { success, message, data: [UserDTO] }
                        com.fasterxml.jackson.databind.JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in get followers response");
                            return new java.util.ArrayList<>();
                        }
                        
                        return mapper.readValue(dataNode.toString(), new TypeReference<List<User>>() {});
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse followers: " + e.getMessage());
                        e.printStackTrace();
                        return new java.util.ArrayList<>();
                    }
                });
    }
    
    /**
     * Get users that a user is following
     */
    public static CompletableFuture<List<User>> getFollowing(String userId, String token) {
        return HttpClientUtil.get("/follow/" + userId + "/following", token, com.fasterxml.jackson.databind.JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Get following response: " + response.toString());
                        // Backend returns: { success, message, data: [UserDTO] }
                        com.fasterxml.jackson.databind.JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in get following response");
                            return new java.util.ArrayList<>();
                        }
                        
                        return mapper.readValue(dataNode.toString(), new TypeReference<List<User>>() {});
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse following: " + e.getMessage());
                        e.printStackTrace();
                        return new java.util.ArrayList<>();
                    }
                });
    }
    
    /**
     * Check if current user is following another user
     */
    public static CompletableFuture<Boolean> isFollowing(String userId, String token) {
        return HttpClientUtil.get("/follow/check/" + userId, token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Check following response: " + response.toString());
                        // Backend returns: { success, message, data: { isFollowing: true/false } }
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in check following response");
                            return false;
                        }
                        
                        JsonNode isFollowingNode = dataNode.get("isFollowing");
                        return isFollowingNode != null && isFollowingNode.asBoolean();
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse follow status: " + e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                });
    }
    
    /**
     * Get follow counts for a user
     */
    public static CompletableFuture<FollowCounts> getFollowCounts(String userId, String token) {
        return HttpClientUtil.get("/users/" + userId + "/follow-counts", token, FollowCounts.class);
    }
    
    /**
     * Toggle follow (follow if not following, unfollow if following)
     */
    public static CompletableFuture<Boolean> toggleFollow(String userId, String token) {
        return isFollowing(userId, token)
                .thenCompose(following -> {
                    if (following) {
                        return unfollowUser(userId, token).thenApply(v -> false);
                    } else {
                        return followUser(userId, token).thenApply(v -> true);
                    }
                });
    }
    
    /**
     * Get mutual followers (users who follow each other)
     */
    public static CompletableFuture<List<User>> getMutualFollowers(String userId, String token) {
        return HttpClientUtil.get("/users/" + userId + "/mutual-followers", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<User>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse mutual followers", e);
                    }
                });
    }
    
    // DTOs
    public static class FollowCheckResponse {
        public boolean isFollowing;
    }
    
    public static class FollowCounts {
        public int followersCount;
        public int followingCount;
    }
}
