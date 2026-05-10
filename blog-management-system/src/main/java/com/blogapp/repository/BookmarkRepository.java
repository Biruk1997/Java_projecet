package com.blogapp.repository;

import com.blogapp.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
    Optional<Bookmark> findByUserUserIdAndPostPostId(UUID userId, UUID postId);
    Boolean existsByUserUserIdAndPostPostId(UUID userId, UUID postId);
    Page<Bookmark> findByUserUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}
