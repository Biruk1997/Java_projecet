package com.blogapp.dto.request;

import com.blogapp.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @Size(max = 300, message = "Subtitle must not exceed 300 characters")
    private String subtitle;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String contentJson;
    private String coverImage;
    private Post.PostStatus status;
    private Post.PostVisibility visibility;
    private Set<UUID> topicIds;
}
