-- Create Database Script
-- Run this file first before starting the application

CREATE DATABASE IF NOT EXISTS java 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Verify database creation
SHOW DATABASES LIKE 'java';

-- Use the database
USE java;

-- Show current database
SELECT DATABASE();
