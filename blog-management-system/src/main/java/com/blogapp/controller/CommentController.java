package com.blogapp.controller;

import com.blogapp.dto.CommentDTO;
import com.blogapp.dto.request.CommentRequest;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.security.JwtUtil;
import com.blogapp.service.CommentService;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment management endpoints")
public class CommentController {
    
    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/{postId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add comment to post")
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(
            @PathVariable UUID postId,
            @Valid @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        CommentDTO comment = commentService.createComment(postId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added successfully", comment));
    }
    
    @GetMapping("/{postId}")
    @Operation(summary = "Get comments for post")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComments(
            @PathVariable UUID postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<CommentDTO> comments = commentService.getCommentsByPostId(postId, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments.getContent());
        response.put("total", comments.getTotalElements());
        
        return ResponseEntity.ok(ApiResponse.success("Comments retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update comment")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @PathVariable UUID id,
            @Valid @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        CommentDTO comment = commentService.updateComment(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully", comment));
    }
    
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete comment")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = extractUserIdFromToken(authHeader);
        commentService.deleteComment(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully", null));
    }
    
    private UUID extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}
