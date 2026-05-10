package com.blogapp.service;

import com.blogapp.dto.PostDTO;
import com.blogapp.dto.TopicDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.dto.request.PostRequest;
import com.blogapp.entity.Post;
import com.blogapp.entity.Topic;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.exception.UnauthorizedException;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.TopicRepository;
import com.blogapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    
    @Transactional
    public PostDTO createPost(PostRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .content(request.getContent())
                .contentJson(request.getContentJson())
                .coverImage(request.getCoverImage())
                .status(request.getStatus() != null ? request.getStatus() : Post.PostStatus.DRAFT)
                .visibility(request.getVisibility() != null ? request.getVisibility() : Post.PostVisibility.PUBLIC)
                .readingTime(calculateReadingTime(request.getContent()))
                .clapsCount(0)
                .commentsCount(0)
                .viewsCount(0)
                .isFeatured(false)
                .build();
        
        if (request.getTopicIds() != null && !request.getTopicIds().isEmpty()) {
            Set<Topic> topics = new HashSet<>(topicRepository.findAllById(request.getTopicIds()));
            post.setTopics(topics);
        }
        
        if (post.getStatus() == Post.PostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }
        
        post = postRepository.save(post);
        return mapToPostDTO(post);
    }
    
    @Transactional(readOnly = true)
    public Page<PostDTO> getAllPublishedPosts(Pageable pageable) {
        return postRepository.findByStatusOrderByPublishedAtDesc(Post.PostStatus.PUBLISHED, pageable)
                .map(this::mapToPostDTO);
    }
    
    @Transactional(readOnly = true)
    public PostDTO getPostById(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return mapToPostDTO(post);
    }
    
    @Transactional
    public PostDTO updatePost(UUID postId, PostRequest request, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to update this post");
        }
        
        post.setTitle(request.getTitle());
        post.setSubtitle(request.getSubtitle());
        post.setContent(request.getContent());
        post.setContentJson(request.getContentJson());
        post.setCoverImage(request.getCoverImage());
        post.setReadingTime(calculateReadingTime(request.getContent()));
        
        if (request.getStatus() != null) {
            post.setStatus(request.getStatus());
        }
        
        if (request.getVisibility() != null) {
            post.setVisibility(request.getVisibility());
        }
        
        if (request.getTopicIds() != null) {
            Set<Topic> topics = new HashSet<>(topicRepository.findAllById(request.getTopicIds()));
            post.setTopics(topics);
        }
        
        post = postRepository.save(post);
        return mapToPostDTO(post);
    }
    
    @Transactional
    public void deletePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to delete this post");
        }
        
        postRepository.delete(post);
    }
    
    @Transactional
    public PostDTO publishPost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to publish this post");
        }
        
        post.setStatus(Post.PostStatus.PUBLISHED);
        post.setPublishedAt(LocalDateTime.now());
        
        post = postRepository.save(post);
        return mapToPostDTO(post);
    }
    
    @Transactional(readOnly = true)
    public List<PostDTO> getUserDrafts(UUID userId) {
        return postRepository.findDraftsByUserId(userId).stream()
                .map(this::mapToPostDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<PostDTO> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchPosts(keyword, pageable)
                .map(this::mapToPostDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<PostDTO> getPostsByUserId(UUID userId, Pageable pageable) {
        return postRepository.findByUserUserIdAndStatusOrderByCreatedAtDesc(
                userId, Post.PostStatus.PUBLISHED, pageable)
                .map(this::mapToPostDTO);
    }
    
    private Integer calculateReadingTime(String content) {
        int wordCount = content.split("\\s+").length;
        int readingTime = (int) Math.ceil(wordCount / 200.0);
        return Math.max(1, readingTime);
    }
    
    private PostDTO mapToPostDTO(Post post) {
        Set<TopicDTO> topicDTOs = post.getTopics() != null 
                ? post.getTopics().stream().map(this::mapToTopicDTO).collect(Collectors.toSet())
                : new HashSet<>();
        
        return PostDTO.builder()
                .postId(post.getPostId())
                .postNumber(post.getPostNumber())
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .content(post.getContent())
                .contentJson(post.getContentJson())
                .coverImage(post.getCoverImage())
                .status(post.getStatus())
                .visibility(post.getVisibility())
                .readingTime(post.getReadingTime())
                .clapsCount(post.getClapsCount())
                .commentsCount(post.getCommentsCount())
                .viewsCount(post.getViewsCount())
                .isFeatured(post.getIsFeatured())
                .publishedAt(post.getPublishedAt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .author(mapToUserDTO(post.getUser()))
                .topics(topicDTOs)
                .build();
    }
    
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .build();
    }
    
    private TopicDTO mapToTopicDTO(Topic topic) {
        return TopicDTO.builder()
                .topicId(topic.getTopicId())
                .name(topic.getName())
                .slug(topic.getSlug())
                .description(topic.getDescription())
                .imageUrl(topic.getImageUrl())
                .followersCount(topic.getFollowersCount())
                .postsCount(topic.getPostsCount())
                .createdAt(topic.getCreatedAt())
                .build();
    }
}
