package com.blogapp.service;

import com.blogapp.dto.UserDTO;
import com.blogapp.entity.Follower;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.repository.FollowerRepository;
import com.blogapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    
    /**
     * Follow a user
     */
    @Transactional
    public void followUser(UUID followerId, UUID followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
        
        // Check if already following
        if (followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalArgumentException("Already following this user");
        }
        
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("User to follow not found"));
        
        Follower follow = Follower.builder()
                .followerId(followerId)
                .followingId(followingId)
                .follower(follower)
                .following(following)
                .build();
        
        followerRepository.save(follow);
    }
    
    /**
     * Unfollow a user
     */
    @Transactional
    public void unfollowUser(UUID followerId, UUID followingId) {
        Follower follow = followerRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow relationship not found"));
        
        followerRepository.delete(follow);
    }
    
    /**
     * Get followers of a user
     */
    public List<UserDTO> getFollowers(UUID userId) {
        List<Follower> followers = followerRepository.findFollowersByUserId(userId);
        return followers.stream()
                .map(f -> convertToDTO(f.getFollower()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get users that a user is following
     */
    public List<UserDTO> getFollowing(UUID userId) {
        List<Follower> following = followerRepository.findFollowingByUserId(userId);
        return following.stream()
                .map(f -> convertToDTO(f.getFollowing()))
                .collect(Collectors.toList());
    }
    
    /**
     * Check if a user is following another user
     */
    public boolean isFollowing(UUID followerId, UUID followingId) {
        return followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
    
    /**
     * Get follower and following counts
     */
    public FollowCounts getFollowCounts(UUID userId) {
        int followersCount = followerRepository.findFollowersByUserId(userId).size();
        int followingCount = followerRepository.findFollowingByUserId(userId).size();
        
        return new FollowCounts(followersCount, followingCount);
    }
    
    /**
     * Toggle follow status
     */
    @Transactional
    public boolean toggleFollow(UUID followerId, UUID followingId) {
        if (isFollowing(followerId, followingId)) {
            unfollowUser(followerId, followingId);
            return false;
        } else {
            followUser(followerId, followingId);
            return true;
        }
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
        return dto;
    }
    
    // DTO
    public static class FollowCounts {
        public int followersCount;
        public int followingCount;
        
        public FollowCounts(int followersCount, int followingCount) {
            this.followersCount = followersCount;
            this.followingCount = followingCount;
        }
    }
}
