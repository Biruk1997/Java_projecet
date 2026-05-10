-- V5: Create Comments Table (with threading support)
CREATE TABLE comments (
    comment_id CHAR(36) PRIMARY KEY,
    comment_number BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
    post_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    parent_id CHAR(36) NULL,
    content TEXT NOT NULL,
    claps_count INT DEFAULT 0,
    is_highlighted BOOLEAN DEFAULT FALSE,
    highlight_start INT,
    highlight_end INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
    INDEX idx_comments_post (post_id),
    INDEX idx_comments_user (user_id),
    INDEX idx_comments_parent (parent_id),
    INDEX idx_comments_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
