package com.blogapp.controller;

import com.blogapp.dto.response.ApiResponse;
import com.blogapp.security.JwtUtil;
import com.blogapp.service.ClapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/claps")
@RequiredArgsConstructor
@Tag(name = "Claps", description = "Clap (like) management endpoints")
public class ClapController {
    
    private final ClapService clapService;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/{postId}/toggle")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Toggle clap on post")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleClap(
            @PathVariable UUID postId,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        ClapService.ToggleClapResponse result = clapService.toggleClap(postId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("clapped", result.isClapped());
        response.put("clapsCount", result.getClapsCount());
        
        return ResponseEntity.ok(ApiResponse.success(
                result.isClapped() ? "Clap added successfully" : "Clap removed successfully", 
                response));
    }
    
    @PostMapping("/{postId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add clap to post")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> addClap(
            @PathVariable UUID postId,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        Integer clapsCount = clapService.addClap(postId, userId);
        
        Map<String, Integer> response = new HashMap<>();
        response.put("claps_count", clapsCount);
        
        return ResponseEntity.ok(ApiResponse.success("Clap added successfully", response));
    }
    
    @DeleteMapping("/{postId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove clap from post")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> removeClap(
            @PathVariable UUID postId,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        Integer clapsCount = clapService.removeClap(postId, userId);
        
        Map<String, Integer> response = new HashMap<>();
        response.put("claps_count", clapsCount);
        
        return ResponseEntity.ok(ApiResponse.success("Clap removed successfully", response));
    }
    
    @GetMapping("/{postId}/count")
    @Operation(summary = "Get claps count")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getClapsCount(@PathVariable UUID postId) {
        Integer clapsCount = clapService.getClapsCount(postId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", postId);
        response.put("claps_count", clapsCount);
        
        return ResponseEntity.ok(ApiResponse.success("Claps count retrieved", response));
    }
    
    @GetMapping("/{postId}/user")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Check if user has clapped")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> hasUserClapped(
            @PathVariable UUID postId,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        Boolean hasClapped = clapService.hasUserClapped(postId, userId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("has_clapped", hasClapped);
        
        return ResponseEntity.ok(ApiResponse.success("Clap status retrieved", response));
    }
    
    private UUID extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}
