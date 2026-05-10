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
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", length = 36, columnDefinition = "CHAR(36)")
    private UUID userId;
    
    @Column(name = "user_number", unique = true, nullable = false)
    private Long userNumber;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(length = 255)
    private String password;
    
    @Column(length = 50)
    private String firstname;
    
    @Column(length = 50)
    private String lastname;
    
    @Column(name = "display_name", length = 100)
    private String displayName;
    
    @Column(columnDefinition = "TEXT")
    private String avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=default";
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @Column(name = "oauth_provider", length = 20)
    private String oauthProvider;
    
    @Column(name = "oauth_id", length = 255)
    private String oauthId;
    
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    
    @Column(name = "is_premium")
    private Boolean isPremium = false;
    
    @Column(name = "is_admin")
    private Boolean isAdmin = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Clap> claps = new HashSet<>();
    
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follower> following = new HashSet<>();
    
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follower> followers = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bookmark> bookmarks = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();
}
