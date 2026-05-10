package com.blogapp.dto;

import com.blogapp.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private UUID postId;
    private Long postNumber;
    private String title;
    private String subtitle;
    private String content;
    private String contentJson;
    private String coverImage;
    private Post.PostStatus status;
    private Post.PostVisibility visibility;
    private Integer readingTime;
    private Integer clapsCount;
    private Integer commentsCount;
    private Integer viewsCount;
    private Boolean isFeatured;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO author;
    private Set<TopicDTO> topics;
}
