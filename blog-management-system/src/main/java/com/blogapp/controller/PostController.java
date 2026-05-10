package com.blogapp.controller;

import com.blogapp.dto.PostDTO;
import com.blogapp.dto.request.PostRequest;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.security.JwtUtil;
import com.blogapp.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Post management endpoints")
public class PostController {
    
    private final PostService postService;
    private final JwtUtil jwtUtil;
    
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create new post")
    public ResponseEntity<ApiResponse<PostDTO>> createPost(
            @Valid @RequestBody PostRequest request,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        PostDTO post = postService.createPost(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post created successfully", post));
    }
    
    @GetMapping
    @Operation(summary = "Get all published posts")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<PostDTO> posts = postService.getAllPublishedPosts(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts.getContent());
        response.put("total", posts.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        
        return ResponseEntity.ok(ApiResponse.success("Posts retrieved successfully", response));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get single post")
    public ResponseEntity<ApiResponse<PostDTO>> getPostById(@PathVariable UUID id) {
        PostDTO post = postService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success("Post retrieved successfully", post));
    }
    
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update post")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody PostRequest request,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        PostDTO post = postService.updatePost(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", post));
    }
    
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete post")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        postService.deletePost(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully", null));
    }
    
    @PostMapping("/{id}/publish")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Publish draft post")
    public ResponseEntity<ApiResponse<PostDTO>> publishPost(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        PostDTO post = postService.publishPost(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Post published successfully", post));
    }
    
    @GetMapping("/user/drafts")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get user drafts")
    public ResponseEntity<ApiResponse<Map<String, List<PostDTO>>>> getUserDrafts(
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        List<PostDTO> drafts = postService.getUserDrafts(userId);
        
        Map<String, List<PostDTO>> response = new HashMap<>();
        response.put("drafts", drafts);
        
        return ResponseEntity.ok(ApiResponse.success("Drafts retrieved successfully", response));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search posts")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<PostDTO> posts = postService.searchPosts(q, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts.getContent());
        response.put("total", posts.getTotalElements());
        
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }
    
    private UUID extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}
