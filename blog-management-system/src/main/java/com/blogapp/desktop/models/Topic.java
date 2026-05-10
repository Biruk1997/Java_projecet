package com.blogapp.desktop.models;

import java.time.LocalDateTime;

/**
 * Topic model for desktop application
 */
public class Topic {
    private String topicId;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Integer followersCount;
    private Integer postsCount;
    private LocalDateTime createdAt;
    
    // Constructors
    public Topic() {}
    
    public Topic(String topicId, String name, String slug) {
        this.topicId = topicId;
        this.name = name;
        this.slug = slug;
    }
    
    // Getters and Setters
    public String getTopicId() { return topicId; }
    public void setTopicId(String topicId) { this.topicId = topicId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Integer getFollowersCount() { return followersCount != null ? followersCount : 0; }
    public void setFollowersCount(Integer followersCount) { this.followersCount = followersCount; }
    
    public Integer getPostsCount() { return postsCount != null ? postsCount : 0; }
    public void setPostsCount(Integer postsCount) { this.postsCount = postsCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Topic{" +
                "topicId='" + topicId + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
