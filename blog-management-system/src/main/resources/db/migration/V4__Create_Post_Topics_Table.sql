-- V4: Create Post Topics Junction Table (Many-to-Many)
CREATE TABLE post_topics (
    post_id CHAR(36) NOT NULL,
    topic_id CHAR(36) NOT NULL,
    PRIMARY KEY (post_id, topic_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (topic_id) REFERENCES topics(topic_id) ON DELETE CASCADE,
    INDEX idx_post_topics_topic (topic_id),
    INDEX idx_post_topics_post (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
