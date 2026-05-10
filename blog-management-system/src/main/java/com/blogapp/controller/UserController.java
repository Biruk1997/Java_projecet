package com.blogapp.controller;

import com.blogapp.dto.UserDTO;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * Get all users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }
    
    /**
     * Search users by name or username
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDTO>>> searchUsers(@RequestParam("q") String query) {
        List<UserDTO> users = userService.searchUsers(query);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", users));
    }
    
    /**
     * Get user by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable String userId) {
        UUID userUuid = UUID.fromString(userId);
        UserDTO user = userService.getUserById(userUuid);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }
}
