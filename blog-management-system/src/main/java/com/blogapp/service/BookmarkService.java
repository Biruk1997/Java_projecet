package com.blogapp.service;

import com.blogapp.dto.PostDTO;
import com.blogapp.entity.Bookmark;
import com.blogapp.entity.Post;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.repository.BookmarkRepository;
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
public class BookmarkService {
    
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public void addBookmark(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!bookmarkRepository.existsByUserUserIdAndPostPostId(userId, postId)) {
            Bookmark bookmark = Bookmark.builder()
                    .post(post)
                    .user(user)
                    .build();
            bookmarkRepository.save(bookmark);
        }
    }
    
    @Transactional
    public void removeBookmark(UUID postId, UUID userId) {
        Bookmark bookmark = bookmarkRepository.findByUserUserIdAndPostPostId(userId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));
        
        bookmarkRepository.delete(bookmark);
    }
    
    @Transactional(readOnly = true)
    public Boolean isBookmarked(UUID postId, UUID userId) {
        return bookmarkRepository.existsByUserUserIdAndPostPostId(userId, postId);
    }
    
    @Transactional(readOnly = true)
    public Page<PostDTO> getUserBookmarks(UUID userId, Pageable pageable) {
        return bookmarkRepository.findByUserUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(bookmark -> mapToPostDTO(bookmark.getPost()));
    }
    
    private PostDTO mapToPostDTO(Post post) {
        return PostDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .coverImage(post.getCoverImage())
                .readingTime(post.getReadingTime())
                .clapsCount(post.getClapsCount())
                .commentsCount(post.getCommentsCount())
                .viewsCount(post.getViewsCount())
                .publishedAt(post.getPublishedAt())
                .build();
    }
}
