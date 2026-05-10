package com.blogapp.controller;

import com.blogapp.dto.UserDTO;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.security.JwtUtil;
import com.blogapp.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    
    private final FollowService followService;
    private final JwtUtil jwtUtil;
    
    /**
     * Follow a user
     */
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> followUser(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        
        String jwt = token.replace("Bearer ", "");
        UUID followerId = jwtUtil.extractUserId(jwt);
        UUID followingId = UUID.fromString(userId);
        
        followService.followUser(followerId, followingId);
        
        return ResponseEntity.ok(ApiResponse.success("User followed successfully", null));
    }
    
    /**
     * Unfollow a user
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> unfollowUser(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        
        String jwt = token.replace("Bearer ", "");
        UUID followerId = jwtUtil.extractUserId(jwt);
        UUID followingId = UUID.fromString(userId);
        
        followService.unfollowUser(followerId, followingId);
        
        return ResponseEntity.ok(ApiResponse.success("User unfollowed successfully", null));
    }
    
    /**
     * Toggle follow status
     */
    @PostMapping("/{userId}/toggle")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> toggleFollow(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        
        String jwt = token.replace("Bearer ", "");
        UUID followerId = jwtUtil.extractUserId(jwt);
        UUID followingId = UUID.fromString(userId);
        
        boolean isFollowing = followService.toggleFollow(followerId, followingId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        
        return ResponseEntity.ok(ApiResponse.success("Follow status toggled", response));
    }
    
    /**
     * Check if following a user
     */
    @GetMapping("/check/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkFollowing(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        
        String jwt = token.replace("Bearer ", "");
        UUID followerId = jwtUtil.extractUserId(jwt);
        UUID followingId = UUID.fromString(userId);
        
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        
        return ResponseEntity.ok(ApiResponse.success("Follow status retrieved", response));
    }
    
    /**
     * Get followers of a user
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getFollowers(@PathVariable String userId) {
        UUID userUuid = UUID.fromString(userId);
        List<UserDTO> followers = followService.getFollowers(userUuid);
        
        return ResponseEntity.ok(ApiResponse.success("Followers retrieved successfully", followers));
    }
    
    /**
     * Get users that a user is following
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getFollowing(@PathVariable String userId) {
        UUID userUuid = UUID.fromString(userId);
        List<UserDTO> following = followService.getFollowing(userUuid);
        
        return ResponseEntity.ok(ApiResponse.success("Following retrieved successfully", following));
    }
    
    /**
     * Get follow counts
     */
    @GetMapping("/{userId}/counts")
    public ResponseEntity<ApiResponse<FollowService.FollowCounts>> getFollowCounts(@PathVariable String userId) {
        UUID userUuid = UUID.fromString(userId);
        FollowService.FollowCounts counts = followService.getFollowCounts(userUuid);
        
        return ResponseEntity.ok(ApiResponse.success("Follow counts retrieved successfully", counts));
    }
}
