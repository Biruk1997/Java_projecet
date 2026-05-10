package com.blogapp.service;

import com.blogapp.dto.CommentDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.dto.request.CommentRequest;
import com.blogapp.entity.Comment;
import com.blogapp.entity.Post;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.exception.UnauthorizedException;
import com.blogapp.repository.CommentRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CommentDTO createComment(UUID postId, CommentRequest request, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .clapsCount(0)
                .isHighlighted(false)
                .build();
        
        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            comment.setParent(parent);
        }
        
        comment = commentRepository.save(comment);
        
        // Update post comment count
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);
        
        return mapToDTO(comment);
    }
    
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByPostId(UUID postId, Pageable pageable) {
        return commentRepository.findByPostPostIdAndParentIsNullOrderByCreatedAtDesc(postId, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional
    public CommentDTO updateComment(UUID commentId, CommentRequest request, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to update this comment");
        }
        
        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);
        
        return mapToDTO(comment);
    }
    
    @Transactional
    public void deleteComment(UUID commentId, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to delete this comment");
        }
        
        // Update post comment count
        Post post = comment.getPost();
        post.setCommentsCount(Math.max(0, post.getCommentsCount() - 1));
        postRepository.save(post);
        
        commentRepository.delete(comment);
    }
    
    private CommentDTO mapToDTO(Comment comment) {
        return CommentDTO.builder()
                .commentId(comment.getCommentId())
                .commentNumber(comment.getCommentNumber())
                .content(comment.getContent())
                .clapsCount(comment.getClapsCount())
                .isHighlighted(comment.getIsHighlighted())
                .highlightStart(comment.getHighlightStart())
                .highlightEnd(comment.getHighlightEnd())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .author(mapToUserDTO(comment.getUser()))
                .postId(comment.getPost().getPostId())
                .parentId(comment.getParent() != null ? comment.getParent().getCommentId() : null)
                .build();
    }
    
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatar(user.getAvatar())
                .build();
    }
}
