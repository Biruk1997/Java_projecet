package com.blogapp.desktop.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Post model for desktop application
 */
public class Post {
    private String postId;
    private Long postNumber;  // Added missing field
    private String title;
    private String subtitle;
    private String content;
    private String contentJson;
    private String coverImage;
    private String status; // DRAFT, PUBLISHED, UNLISTED, ARCHIVED
    private String visibility; // PUBLIC, FOLLOWERS, PREMIUM
    private Integer readingTime;
    private Integer clapsCount;
    private Integer commentsCount;
    private Integer viewsCount;
    private Boolean isFeatured;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Relationships
    private User author;
    private List<Topic> topics;
    private List<Comment> comments;
    
    // Constructors
    public Post() {
        this.topics = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
    
    public Post(String postId, String title, String content) {
        this();
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
    
    // Getters and Setters
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    
    public Long getPostNumber() { return postNumber; }
    public void setPostNumber(Long postNumber) { this.postNumber = postNumber; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getContentJson() { return contentJson; }
    public void setContentJson(String contentJson) { this.contentJson = contentJson; }
    
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    
    public Integer getReadingTime() { return readingTime != null ? readingTime : 1; }
    public void setReadingTime(Integer readingTime) { this.readingTime = readingTime; }
    
    public Integer getClapsCount() { return clapsCount != null ? clapsCount : 0; }
    public void setClapsCount(Integer clapsCount) { this.clapsCount = clapsCount; }
    
    public Integer getCommentsCount() { return commentsCount != null ? commentsCount : 0; }
    public void setCommentsCount(Integer commentsCount) { this.commentsCount = commentsCount; }
    
    public Integer getViewsCount() { return viewsCount != null ? viewsCount : 0; }
    public void setViewsCount(Integer viewsCount) { this.viewsCount = viewsCount; }
    
    public Boolean getIsFeatured() { return isFeatured != null ? isFeatured : false; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    
    public List<Topic> getTopics() { return topics; }
    public void setTopics(List<Topic> topics) { this.topics = topics; }
    
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    
    // Helper methods
    public String getExcerpt(int maxLength) {
        if (content == null) return "";
        String plainText = content.replaceAll("<[^>]*>", "").trim();
        if (plainText.length() <= maxLength) {
            return plainText;
        }
        return plainText.substring(0, maxLength) + "...";
    }
    
    public boolean isDraft() {
        return "DRAFT".equalsIgnoreCase(status);
    }
    
    public boolean isPublished() {
        return "PUBLISHED".equalsIgnoreCase(status);
    }
    
    public String getFormattedDate() {
        LocalDateTime date = publishedAt != null ? publishedAt : createdAt;
        if (date == null) return "";
        
        java.time.Duration duration = java.time.Duration.between(date, LocalDateTime.now());
        long days = duration.toDays();
        
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " weeks ago";
        if (days < 365) return (days / 30) + " months ago";
        return (days / 365) + " years ago";
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", author=" + (author != null ? author.getUsername() : "null") +
                '}';
    }
}
