package com.blogapp.desktop.services;

import com.blogapp.desktop.models.Post;
import com.blogapp.desktop.models.Topic;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Topic Service - Handles topic operations
 * Equivalent to React's topicService.jsx
 */
public class TopicService {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Get all topics
     */
    public static CompletableFuture<List<Topic>> getAllTopics(String token) {
        return HttpClientUtil.get("/topics", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Topic>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse topics", e);
                    }
                });
    }
    
    /**
     * Get trending topics
     */
    public static CompletableFuture<List<Topic>> getTrendingTopics(String token) {
        return HttpClientUtil.get("/topics/trending", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Topic>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse trending topics", e);
                    }
                });
    }
    
    /**
     * Get topic by slug
     */
    public static CompletableFuture<Topic> getTopicBySlug(String slug, String token) {
        return HttpClientUtil.get("/topics/" + slug, token, Topic.class);
    }
    
    /**
     * Get posts by topic
     */
    public static CompletableFuture<List<Post>> getPostsByTopic(String topicSlug, String token) {
        return HttpClientUtil.get("/topics/" + topicSlug + "/posts", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse posts by topic", e);
                    }
                });
    }
    
    /**
     * Follow a topic
     */
    public static CompletableFuture<Void> followTopic(String topicId, String token) {
        Map<String, String> body = new HashMap<>();
        body.put("topicId", topicId);
        
        return HttpClientUtil.post("/topics/" + topicId + "/follow", body, token, Void.class)
                .thenApply(v -> null);
    }
    
    /**
     * Unfollow a topic
     */
    public static CompletableFuture<Void> unfollowTopic(String topicId, String token) {
        return HttpClientUtil.delete("/topics/" + topicId + "/follow", token);
    }
    
    /**
     * Check if user is following a topic
     */
    public static CompletableFuture<Boolean> isFollowingTopic(String topicId, String token) {
        return HttpClientUtil.get("/topics/" + topicId + "/is-following", token, TopicFollowCheckResponse.class)
                .thenApply(response -> response.isFollowing);
    }
    
    /**
     * Get user's followed topics
     */
    public static CompletableFuture<List<Topic>> getFollowedTopics(String token) {
        return HttpClientUtil.get("/topics/followed", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Topic>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse followed topics", e);
                    }
                });
    }
    
    /**
     * Search topics by query
     */
    public static CompletableFuture<List<Topic>> searchTopics(String query, String token) {
        return HttpClientUtil.get("/topics/search?q=" + query, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Topic>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse topic search results", e);
                    }
                });
    }
    
    // DTOs
    public static class TopicFollowCheckResponse {
        public boolean isFollowing;
    }
}
