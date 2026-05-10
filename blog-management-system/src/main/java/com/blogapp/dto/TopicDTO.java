package com.blogapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private UUID topicId;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Integer followersCount;
    private Integer postsCount;
    private LocalDateTime createdAt;
}
