package com.blogapp.desktop.models;

import java.time.LocalDateTime;

/**
 * Comment model for desktop application
 */
public class Comment {
    private String commentId;
    private Long commentNumber;
    private String content;
    private Integer clapsCount;
    private Boolean isHighlighted;
    private Integer highlightStart;
    private Integer highlightEnd;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User author;
    private String postId;
    private String parentId;
    
    // Constructors
    public Comment() {}
    
    public Comment(String commentId, String content, User author) {
        this.commentId = commentId;
        this.content = content;
        this.author = author;
    }
    
    // Getters and Setters
    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }
    
    public Long getCommentNumber() { return commentNumber; }
    public void setCommentNumber(Long commentNumber) { this.commentNumber = commentNumber; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Integer getClapsCount() { return clapsCount; }
    public void setClapsCount(Integer clapsCount) { this.clapsCount = clapsCount; }
    
    public Boolean getIsHighlighted() { return isHighlighted; }
    public void setIsHighlighted(Boolean isHighlighted) { this.isHighlighted = isHighlighted; }
    
    public Integer getHighlightStart() { return highlightStart; }
    public void setHighlightStart(Integer highlightStart) { this.highlightStart = highlightStart; }
    
    public Integer getHighlightEnd() { return highlightEnd; }
    public void setHighlightEnd(Integer highlightEnd) { this.highlightEnd = highlightEnd; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    
    public String getFormattedDate() {
        if (createdAt == null) return "";
        
        java.time.Duration duration = java.time.Duration.between(createdAt, LocalDateTime.now());
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
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                ", author=" + (author != null ? author.getUsername() : "null") +
                '}';
    }
}
