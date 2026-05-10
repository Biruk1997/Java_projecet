package com.blogapp.desktop.services;

import com.blogapp.desktop.models.Comment;
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
 * Comment Service - Handles post comments
 */
public class CommentService {
    
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * Get comments for a post
     */
    public static CompletableFuture<List<Comment>> getComments(String postId, String token) {
        System.out.println("DEBUG: Getting comments for post: " + postId);
        
        return HttpClientUtil.get("/comments/" + postId, token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Get comments response: " + response.toString());
                        // Backend returns: { success, message, data: { comments, total } }
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in comments response");
                            throw new RuntimeException("No data in response");
                        }
                        
                        JsonNode commentsNode = dataNode.get("comments");
                        if (commentsNode == null) {
                            System.err.println("ERROR: No comments array in response");
                            throw new RuntimeException("No comments in response");
                        }
                        
                        return mapper.readValue(commentsNode.toString(), new TypeReference<List<Comment>>() {});
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse comments: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to parse comments: " + e.getMessage(), e);
                    }
                });
    }
    
    /**
     * Add a comment to a post
     */
    public static CompletableFuture<Comment> addComment(String postId, String content, String token) {
        System.out.println("DEBUG: Adding comment to post: " + postId);
        
        Map<String, String> body = new HashMap<>();
        body.put("content", content);
        
        return HttpClientUtil.post("/comments/" + postId, body, token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Add comment response: " + response.toString());
                        // Backend returns: { success, message, data: comment }
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in add comment response");
                            throw new RuntimeException("No data in response");
                        }
                        
                        Comment comment = mapper.treeToValue(dataNode, Comment.class);
                        System.out.println("DEBUG: Comment added successfully");
                        return comment;
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse comment response: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to parse comment response: " + e.getMessage(), e);
                    }
                });
    }
    
    /**
     * Delete a comment
     */
    public static CompletableFuture<Void> deleteComment(String commentId, String token) {
        System.out.println("DEBUG: Deleting comment: " + commentId);
        return HttpClientUtil.delete("/comments/" + commentId, token);
    }
}
