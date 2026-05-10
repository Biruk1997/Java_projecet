package com.blogapp.desktop.services;

import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Clap Service - Handles clapping/liking posts
 */
public class ClapService {
    
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * Toggle clap on a post
     */
    public static CompletableFuture<ClapResponse> toggleClap(String postId, String token) {
        System.out.println("DEBUG: Toggling clap for post: " + postId);
        
        return HttpClientUtil.post("/claps/" + postId + "/toggle", new HashMap<>(), token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        System.out.println("DEBUG: Clap response: " + response.toString());
                        // Backend returns: { success, message, data: { clapped, clapsCount } }
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            System.err.println("ERROR: No data in clap response");
                            throw new RuntimeException("No data in response");
                        }
                        
                        ClapResponse clapResponse = new ClapResponse();
                        clapResponse.clapped = dataNode.get("clapped").asBoolean();
                        clapResponse.clapsCount = dataNode.get("clapsCount").asInt();
                        
                        System.out.println("DEBUG: Clap successful - clapped: " + clapResponse.clapped + ", count: " + clapResponse.clapsCount);
                        return clapResponse;
                    } catch (Exception e) {
                        System.err.println("ERROR: Failed to parse clap response: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to parse clap response: " + e.getMessage(), e);
                    }
                });
    }
    
    /**
     * Check if user has clapped a post
     */
    public static CompletableFuture<Boolean> hasClapped(String postId, String token) {
        return HttpClientUtil.get("/claps/" + postId + "/status", token, JsonNode.class)
                .thenApply(response -> {
                    try {
                        JsonNode dataNode = response.get("data");
                        if (dataNode == null) {
                            return false;
                        }
                        return dataNode.get("clapped").asBoolean();
                    } catch (Exception e) {
                        return false;
                    }
                });
    }
    
    // DTOs
    public static class ClapResponse {
        public boolean clapped;
        public int clapsCount;
    }
}
