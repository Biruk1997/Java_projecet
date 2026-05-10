package com.blogapp.desktop.services;

import com.blogapp.desktop.models.Post;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Bookmark Service - Handles bookmark operations
 * Equivalent to React's bookmarkService.jsx
 */
public class BookmarkService {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Add bookmark to a post
     */
    public static CompletableFuture<Void> addBookmark(String postId, String token) {
        Map<String, String> body = new HashMap<>();
        body.put("postId", postId);
        
        return HttpClientUtil.post("/bookmarks", body, token, Void.class)
                .thenApply(v -> null);
    }
    
    /**
     * Remove bookmark from a post
     */
    public static CompletableFuture<Void> removeBookmark(String postId, String token) {
        return HttpClientUtil.delete("/bookmarks/" + postId, token);
    }
    
    /**
     * Get user's bookmarked posts
     */
    public static CompletableFuture<List<Post>> getBookmarks(String token) {
        return HttpClientUtil.get("/bookmarks", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Post>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse bookmarks", e);
                    }
                });
    }
    
    /**
     * Check if post is bookmarked
     */
    public static CompletableFuture<Boolean> isBookmarked(String postId, String token) {
        return HttpClientUtil.get("/bookmarks/check/" + postId, token, BookmarkCheckResponse.class)
                .thenApply(response -> response.isBookmarked);
    }
    
    /**
     * Get bookmark count for a post
     */
    public static CompletableFuture<Integer> getBookmarkCount(String postId, String token) {
        return HttpClientUtil.get("/posts/" + postId + "/bookmarks/count", token, BookmarkCountResponse.class)
                .thenApply(response -> response.count);
    }
    
    /**
     * Toggle bookmark (add if not bookmarked, remove if bookmarked)
     */
    public static CompletableFuture<Boolean> toggleBookmark(String postId, String token) {
        return isBookmarked(postId, token)
                .thenCompose(bookmarked -> {
                    if (bookmarked) {
                        return removeBookmark(postId, token).thenApply(v -> false);
                    } else {
                        return addBookmark(postId, token).thenApply(v -> true);
                    }
                });
    }
    
    // DTOs
    public static class BookmarkCheckResponse {
        public boolean isBookmarked;
    }
    
    public static class BookmarkCountResponse {
        public int count;
    }
}
