package com.blogapp.controller;

import com.blogapp.dto.PostDTO;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.security.JwtUtil;
import com.blogapp.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmarks", description = "Bookmark management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class BookmarkController {
    
    private final BookmarkService bookmarkService;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/{postId}")
    @Operation(summary = "Add bookmark")
    public ResponseEntity<ApiResponse<Void>> addBookmark(
            @PathVariable UUID postId,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        bookmarkService.addBookmark(postId, userId);
        return ResponseEntity.ok(ApiResponse.success("Post bookmarked successfully", null));
    }
    
    @DeleteMapping("/{postId}")
    @Operation(summary = "Remove bookmark")
    public ResponseEntity<ApiResponse<Void>> removeBookmark(
            @PathVariable UUID postId,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        bookmarkService.removeBookmark(postId, userId);
        return ResponseEntity.ok(ApiResponse.success("Bookmark removed successfully", null));
    }
    
    @GetMapping("/{postId}/check")
    @Operation(summary = "Check if bookmarked")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkBookmark(
            @PathVariable UUID postId,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        Boolean bookmarked = bookmarkService.isBookmarked(postId, userId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("bookmarked", bookmarked);
        
        return ResponseEntity.ok(ApiResponse.success("Bookmark status retrieved", response));
    }
    
    @GetMapping
    @Operation(summary = "Get user bookmarks")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        Pageable pageable = PageRequest.of(page, limit);
        Page<PostDTO> bookmarks = bookmarkService.getUserBookmarks(userId, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("bookmarks", bookmarks.getContent());
        response.put("total", bookmarks.getTotalElements());
        
        return ResponseEntity.ok(ApiResponse.success("Bookmarks retrieved successfully", response));
    }
    
    private UUID extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}
