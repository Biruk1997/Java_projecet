package com.blogapp.service;

import com.blogapp.dto.UserDTO;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Get all users
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search users by name or username
     */
    public List<UserDTO> searchUsers(String query) {
        String searchQuery = "%" + query.toLowerCase() + "%";
        List<User> users = userRepository.searchByNameOrUsername(searchQuery);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get user by ID
     */
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDTO(user);
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setDisplayName(user.getDisplayName());
        dto.setBio(user.getBio());
        dto.setCreatedAt(user.getCreatedAt());
        
        // Calculate counts
        Integer followersCount = userRepository.countFollowers(user.getUserId());
        Integer followingCount = userRepository.countFollowing(user.getUserId());
        
        dto.setFollowersCount(followersCount != null ? followersCount : 0);
        dto.setFollowingCount(followingCount != null ? followingCount : 0);
        dto.setPostsCount(0); // TODO: Implement post count
        
        return dto;
    }
}
