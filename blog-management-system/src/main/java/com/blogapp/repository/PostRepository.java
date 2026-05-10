package com.blogapp.repository;

import com.blogapp.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    
    Page<Post> findByStatusOrderByPublishedAtDesc(Post.PostStatus status, Pageable pageable);
    
    Page<Post> findByUserUserIdAndStatusOrderByCreatedAtDesc(UUID userId, Post.PostStatus status, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.user.userId = :userId AND p.status = 'DRAFT' ORDER BY p.updatedAt DESC")
    List<Post> findDraftsByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Post p JOIN p.topics t WHERE t.topicId = :topicId AND p.status = 'PUBLISHED' ORDER BY p.publishedAt DESC")
    Page<Post> findByTopicId(@Param("topicId") UUID topicId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND p.isFeatured = true ORDER BY p.publishedAt DESC")
    List<Post> findFeaturedPosts(Pageable pageable);
    
    Integer countByUserUserId(UUID userId);
}
