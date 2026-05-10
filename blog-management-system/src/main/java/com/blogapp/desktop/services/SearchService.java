package com.blogapp.desktop.services;

import com.blogapp.desktop.models.Post;
import com.blogapp.desktop.models.Topic;
import com.blogapp.desktop.models.User;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Search Service - Handles search operations across posts, users, and topics
 * Equivalent to React's search functionality
 */
public class SearchService {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Search posts by query
     */
    public static CompletableFuture<List<Post>> searchPosts(String query, String token) {
        String encodedQuery = encodeQuery(query);
        return HttpClientUtil.get("/search/posts?q=" + encodedQuery, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse post search results", e);
                    }
                });
    }
    
    /**
     * Search users by query
     */
    public static CompletableFuture<List<User>> searchUsers(String query, String token) {
        String encodedQuery = encodeQuery(query);
        return HttpClientUtil.get("/search/users?q=" + encodedQuery, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<User>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse user search results", e);
                    }
                });
    }
    
    /**
     * Search topics by query
     */
    public static CompletableFuture<List<Topic>> searchTopics(String query, String token) {
        String encodedQuery = encodeQuery(query);
        return HttpClientUtil.get("/search/topics?q=" + encodedQuery, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Topic>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse topic search results", e);
                    }
                });
    }
    
    /**
     * Global search (all types)
     */
    public static CompletableFuture<SearchResults> searchAll(String query, String token) {
        String encodedQuery = encodeQuery(query);
        return HttpClientUtil.get("/search/all?q=" + encodedQuery, token, SearchResults.class);
    }
    
    /**
     * Get search suggestions
     */
    public static CompletableFuture<List<String>> getSearchSuggestions(String query, String token) {
        String encodedQuery = encodeQuery(query);
        return HttpClientUtil.get("/search/suggestions?q=" + encodedQuery, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<String>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse search suggestions", e);
                    }
                });
    }
    
    /**
     * Get popular searches
     */
    public static CompletableFuture<List<String>> getPopularSearches(String token) {
        return HttpClientUtil.get("/search/popular", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<String>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse popular searches", e);
                    }
                });
    }
    
    /**
     * URL encode query string
     */
    private static String encodeQuery(String query) {
        try {
            return java.net.URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            return query;
        }
    }
    
    // DTOs
    public static class SearchResults {
        public List<Post> posts;
        public List<User> users;
        public List<Topic> topics;
    }
}
