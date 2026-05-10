-- V1: Create Users Table
CREATE TABLE users (
    user_id CHAR(36) PRIMARY KEY,
    user_number BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255),
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    display_name VARCHAR(100),
    avatar TEXT,
    bio TEXT,
    oauth_provider VARCHAR(20),
    oauth_id VARCHAR(255),
    email_verified BOOLEAN DEFAULT FALSE,
    is_premium BOOLEAN DEFAULT FALSE,
    is_admin BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_oauth (oauth_provider, oauth_id),
    INDEX idx_users_email (email),
    INDEX idx_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
