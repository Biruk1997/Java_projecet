package com.blogapp.desktop;

import com.blogapp.desktop.views.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Pure Java Desktop Application for Blog Platform
 * Built with JavaFX - No HTML, No JavaScript, Pure Java!
 * 
 * NOTE: This is the FRONTEND ONLY. The Spring Boot backend must be running separately.
 * 
 * HOW TO RUN:
 * 1. Start backend first: mvnw spring-boot:run (runs on port 8080)
 * 2. Then start frontend: mvnw javafx:run (opens desktop window)
 */
public class BlogDesktopApplication extends Application {
    
    public static void main(String[] args) {
        // Launch JavaFX GUI only (backend runs separately)
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blog Platform - Login");
        
        // Show login screen using our LoginView component
        LoginView loginView = new LoginView(primaryStage);
        primaryStage.setScene(loginView.createScene());
        
        primaryStage.show();
    }
}
