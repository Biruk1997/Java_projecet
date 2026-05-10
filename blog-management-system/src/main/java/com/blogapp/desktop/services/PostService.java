package com.blogapp.desktop.services;

import com.blogapp.desktop.models.Post;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Post Service - Handles all post-related operations
 * Equivalent to React's postService.jsx
 */
public class PostService {
    
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * Get all posts (public feed)
     */
    public static CompletableFuture<List<Post>> getAllPosts(String token) {
        return HttpClientUtil.get("/posts", token, com.fasterxml.jackson.databind.JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Get all posts response: " + response.toString());
                        // Backend returns: { success, message, data: { posts, total, page, limit } }
                        com.fasterxml.jackson.databind.JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in get posts response");
                            throw new RuntimeException("No data in response");
                        }
                        
                        com.fasterxml.jackson.databind.JsonNode postsNode = dataNode.get("posts");
                        if (postsNode == null) {
                            System.err.println("ERROR: No posts in data");
                            throw new RuntimeException("No posts in data");
                        }
                        
                        return mapper.readValue(postsNode.toString(), new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse posts: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to parse posts: " + e.getMessage(), e);
                    }
                });
    }
    
    /**
     * Get post by ID
     */
    public static CompletableFuture<Post> getPostById(String postId, String token) {
        return HttpClientUtil.get("/posts/" + postId, token, Post.class);
    }
    
    /**
     * Create new post
     */
    public static CompletableFuture<Post> createPost(CreatePostRequest request, String token) {
        Map<String, Object> body = new HashMap<>();
        body.put("title", request.title);
        body.put("subtitle", request.subtitle);
        body.put("content", request.content);
        body.put("coverImage", request.coverImage);
        body.put("topicIds", request.topics); // Backend expects topicIds
        body.put("status", request.isDraft ? "DRAFT" : "PUBLISHED");
        body.put("visibility", "PUBLIC");
        
        System.out.println("DEBUG: Creating post with title: " + request.title);
        
        return HttpClientUtil.post("/posts", body, token, com.fasterxml.jackson.databind.JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Create post response: " + response.toString());
                        // Backend returns: { success, message, data: PostDTO }
                        com.fasterxml.jackson.databind.JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in create post response");
                            throw new RuntimeException("No data in response");
                        }
                        return mapper.treeToValue(dataNode, Post.class);
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse create post response: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to parse post response: " + e.getMessage(), e);
                    }
                });
    }
    
    /**
     * Update existing post
     */
    public static CompletableFuture<Post> updatePost(String postId, UpdatePostRequest request, String token) {
        Map<String, Object> body = new HashMap<>();
        body.put("title", request.title);
        body.put("subtitle", request.subtitle);
        body.put("content", request.content);
        body.put("coverImage", request.coverImage);
        body.put("topicIds", request.topics); // Backend expects topicIds
        body.put("status", request.isDraft ? "DRAFT" : "PUBLISHED");
        body.put("visibility", "PUBLIC");
        
        return HttpClientUtil.put("/posts/" + postId, body, token, Post.class);
    }
    
    /**
     * Delete post
     */
    public static CompletableFuture<Void> deletePost(String postId, String token) {
        return HttpClientUtil.delete("/posts/" + postId, token);
    }
    
    /**
     * Get user's draft posts
     */
    public static CompletableFuture<List<Post>> getDrafts(String token) {
        return HttpClientUtil.get("/posts/drafts", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse drafts", e);
                    }
                });
    }
    
    /**
     * Search posts by query
     */
    public static CompletableFuture<List<Post>> searchPosts(String query, String token) {
        return HttpClientUtil.get("/posts/search?q=" + query, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse search results", e);
                    }
                });
    }
    
    /**
     * Get posts by topic
     */
    public static CompletableFuture<List<Post>> getPostsByTopic(String topicSlug, String token) {
        return HttpClientUtil.get("/posts/topic/" + topicSlug, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse posts by topic", e);
                    }
                });
    }
    
    /**
     * Get posts by user
     */
    public static CompletableFuture<List<Post>> getUserPosts(String userId, String token) {
        return HttpClientUtil.get("/posts/user/" + userId, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse user posts", e);
                    }
                });
    }
    
    /**
     * Get feed for authenticated user (following + recommended)
     */
    public static CompletableFuture<List<Post>> getFeed(String token) {
        return HttpClientUtil.get("/posts/feed", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse feed", e);
                    }
                });
    }
    
    /**
     * Publish draft post
     */
    public static CompletableFuture<Post> publishPost(String postId, String token) {
        Map<String, Object> body = new HashMap<>();
        body.put("isDraft", false);
        return HttpClientUtil.put("/posts/" + postId, body, token, Post.class);
    }
    
    // DTOs
    public static class CreatePostRequest {
        public String title;
        public String subtitle;
        public String content;
        public String coverImage;
        public List<String> topics;
        public boolean isDraft = false;
    }
    
    public static class UpdatePostRequest {
        public String title;
        public String subtitle;
        public String content;
        public String coverImage;
        public List<String> topics;
        public boolean isDraft;
    }
    
    /**
     * Get user's draft posts
     */
    public static CompletableFuture<List<Post>> getUserDrafts(String token) {
        System.out.println("DEBUG: Getting user drafts");
        
        return HttpClientUtil.get("/posts/my-posts?status=DRAFT", token, com.fasterxml.jackson.databind.JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Get drafts response: " + response.toString());
                        com.fasterxml.jackson.databind.JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            return new java.util.ArrayList<>();
                        }
                        
                        com.fasterxml.jackson.databind.JsonNode postsNode = dataNode.get("posts");
                        if (postsNode == null) {
                            return new java.util.ArrayList<>();
                        }
                        
                        return mapper.readValue(postsNode.toString(), new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse drafts: " + e.getMessage());
                        return new java.util.ArrayList<>();
                    }
                });
    }
}