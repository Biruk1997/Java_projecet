package com.blogapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnectivityChecker {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "14263208@aA";
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Database Connectivity Checker");
        System.out.println("========================================");
        System.out.println();
        
        // Step 1: Check MySQL Driver
        System.out.println("Step 1: Checking MySQL Driver...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found: " + e.getMessage());
            System.out.println("   Please ensure MySQL Connector/J is in your classpath");
            return;
        }
        System.out.println();
        
        // Step 2: Test connection to MySQL server (without database)
        System.out.println("Step 2: Testing MySQL Server Connection...");
        String serverUrl = "jdbc:mysql://localhost:3306?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        try (Connection conn = DriverManager.getConnection(serverUrl, DB_USER, DB_PASSWORD)) {
            System.out.println("✅ Successfully connected to MySQL Server");
            System.out.println("   Host: localhost:3306");
            System.out.println("   User: " + DB_USER);
            
            // Get MySQL version
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT VERSION()")) {
                if (rs.next()) {
                    System.out.println("   MySQL Version: " + rs.getString(1));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to connect to MySQL Server");
            System.out.println("   Error: " + e.getMessage());
            System.out.println();
            System.out.println("Troubleshooting:");
            System.out.println("1. Check if MySQL is running:");
            System.out.println("   - Windows: Services → MySQL80");
            System.out.println("   - Command: net start MySQL80");
            System.out.println("2. Verify MySQL is listening on port 3306");
            System.out.println("3. Check username and password are correct");
            System.out.println("   Current: root / 14263208@aA");
            return;
        }
        System.out.println();
        
        // Step 3: Check if database exists
        System.out.println("Step 3: Checking if database 'java' exists...");
        try (Connection conn = DriverManager.getConnection(serverUrl, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'java'")) {
            
            if (rs.next()) {
                System.out.println("✅ Database 'java' exists");
            } else {
                System.out.println("⚠️  Database 'java' does not exist");
                System.out.println("   Creating database...");
                
                try (Statement createStmt = conn.createStatement()) {
                    createStmt.executeUpdate(
                        "CREATE DATABASE java CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
                    );
                    System.out.println("✅ Database 'java' created successfully");
                } catch (Exception e) {
                    System.out.println("❌ Failed to create database: " + e.getMessage());
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error checking database: " + e.getMessage());
            return;
        }
        System.out.println();
        
        // Step 4: Test connection to the database
        System.out.println("Step 4: Testing connection to database 'java'...");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("✅ Successfully connected to database 'java'");
            
            // Check if tables exist
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                
                int tableCount = 0;
                System.out.println("   Existing tables:");
                while (rs.next()) {
                    System.out.println("   - " + rs.getString(1));
                    tableCount++;
                }
                
                if (tableCount == 0) {
                    System.out.println("   (No tables found - migrations need to run)");
                } else {
                    System.out.println("   Total tables: " + tableCount);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to connect to database 'java'");
            System.out.println("   Error: " + e.getMessage());
            return;
        }
        System.out.println();
        
        // Step 5: Test write permissions
        System.out.println("Step 5: Testing write permissions...");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Try to create a test table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS connectivity_test (id INT PRIMARY KEY, test VARCHAR(50))");
            stmt.executeUpdate("INSERT INTO connectivity_test VALUES (1, 'test') ON DUPLICATE KEY UPDATE test='test'");
            
            // Verify the insert
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM connectivity_test WHERE id = 1")) {
                if (rs.next()) {
                    System.out.println("✅ Write permissions verified");
                }
            }
            
            // Clean up
            stmt.executeUpdate("DROP TABLE connectivity_test");
            
        } catch (Exception e) {
            System.out.println("❌ Write permission test failed: " + e.getMessage());
            return;
        }
        System.out.println();
        
        // Final Summary
        System.out.println("========================================");
        System.out.println("✅ ALL CHECKS PASSED!");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Database Configuration:");
        System.out.println("  URL: " + DB_URL);
        System.out.println("  User: " + DB_USER);
        System.out.println("  Database: java");
        System.out.println();
        System.out.println("Next Steps:");
        System.out.println("1. Run the application: mvn spring-boot:run");
        System.out.println("2. Flyway will automatically run all migrations");
        System.out.println("3. Access Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println();
    }
}
