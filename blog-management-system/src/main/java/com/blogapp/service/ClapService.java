package com.blogapp.service;

import com.blogapp.entity.Clap;
import com.blogapp.entity.Post;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.repository.ClapRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClapService {
    
    private final ClapRepository clapRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public Integer addClap(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Clap clap = clapRepository.findByPostPostIdAndUserUserId(postId, userId)
                .orElse(Clap.builder()
                        .post(post)
                        .user(user)
                        .count(0)
                        .build());
        
        if (clap.getCount() < 50) {
            clap.setCount(clap.getCount() + 1);
            clapRepository.save(clap);
            
            // Update post claps count
            Integer totalClaps = clapRepository.countClapsByPostId(postId);
            post.setClapsCount(totalClaps != null ? totalClaps : 0);
            postRepository.save(post);
        }
        
        return post.getClapsCount();
    }
    
    @Transactional
    public Integer removeClap(UUID postId, UUID userId) {
        Clap clap = clapRepository.findByPostPostIdAndUserUserId(postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Clap not found"));
        
        clapRepository.delete(clap);
        
        // Update post claps count
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        Integer totalClaps = clapRepository.countClapsByPostId(postId);
        post.setClapsCount(totalClaps != null ? totalClaps : 0);
        postRepository.save(post);
        
        return post.getClapsCount();
    }
    
    @Transactional(readOnly = true)
    public Integer getClapsCount(UUID postId) {
        Integer count = clapRepository.countClapsByPostId(postId);
        return count != null ? count : 0;
    }
    
    @Transactional(readOnly = true)
    public Boolean hasUserClapped(UUID postId, UUID userId) {
        return clapRepository.existsByPostPostIdAndUserUserId(postId, userId);
    }
    
    @Transactional
    public ToggleClapResponse toggleClap(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        boolean hasClapped = clapRepository.existsByPostPostIdAndUserUserId(postId, userId);
        
        if (hasClapped) {
            // Remove clap
            Clap clap = clapRepository.findByPostPostIdAndUserUserId(postId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Clap not found"));
            clapRepository.delete(clap);
        } else {
            // Add clap
            Clap clap = Clap.builder()
                    .post(post)
                    .user(user)
                    .count(1)
                    .build();
            clapRepository.save(clap);
        }
        
        // Update post claps count
        Integer totalClaps = clapRepository.countClapsByPostId(postId);
        post.setClapsCount(totalClaps != null ? totalClaps : 0);
        postRepository.save(post);
        
        return new ToggleClapResponse(!hasClapped, post.getClapsCount());
    }
    
    public static class ToggleClapResponse {
        private final boolean clapped;
        private final int clapsCount;
        
        public ToggleClapResponse(boolean clapped, int clapsCount) {
            this.clapped = clapped;
            this.clapsCount = clapsCount;
        }
        
        public boolean isClapped() {
            return clapped;
        }
        
        public int getClapsCount() {
            return clapsCount;
        }
    }
}
