package com.blogapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts", indexes = {
    @Index(name = "idx_posts_user", columnList = "user_id"),
    @Index(name = "idx_posts_status", columnList = "status"),
    @Index(name = "idx_posts_published", columnList = "published_at")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID postId;
    
    @Column(name = "post_number", unique = true, nullable = false)
    private Long postNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 300)
    private String subtitle;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "content_json", columnDefinition = "TEXT")
    private String contentJson;
    
    @Column(name = "cover_image", columnDefinition = "TEXT")
    private String coverImage;
    
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;
    
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PostVisibility visibility = PostVisibility.PUBLIC;
    
    @Column(name = "reading_time")
    private Integer readingTime = 1;
    
    @Column(name = "claps_count")
    private Integer clapsCount = 0;
    
    @Column(name = "comments_count")
    private Integer commentsCount = 0;
    
    @Column(name = "views_count")
    private Integer viewsCount = 0;
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToMany
    @JoinTable(
        name = "post_topics",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Clap> claps = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bookmark> bookmarks = new HashSet<>();
    
    public enum PostStatus {
        DRAFT, PUBLISHED, UNLISTED, ARCHIVED
    }
    
    public enum PostVisibility {
        PUBLIC, FOLLOWERS, PREMIUM
    }
}
