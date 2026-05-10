package com.blogapp.desktop.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * NavigationManager - Manage navigation between views
 */
public class NavigationManager {
    
    private static Stage primaryStage;
    private static Stack<Scene> navigationStack = new Stack<>();
    
    /**
     * Initialize with primary stage
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
    }
    
    /**
     * Navigate to a new scene
     */
    public static void navigateTo(Scene scene) {
        if (primaryStage != null) {
            Scene currentScene = primaryStage.getScene();
            if (currentScene != null) {
                navigationStack.push(currentScene);
            }
            primaryStage.setScene(scene);
        }
    }
    
    /**
     * Navigate to a new scene with title
     */
    public static void navigateTo(Scene scene, String title) {
        navigateTo(scene);
        if (primaryStage != null) {
            primaryStage.setTitle(title);
        }
    }
    
    /**
     * Go back to previous scene
     */
    public static void goBack() {
        if (!navigationStack.isEmpty() && primaryStage != null) {
            Scene previousScene = navigationStack.pop();
            primaryStage.setScene(previousScene);
        }
    }
    
    /**
     * Get current scene
     */
    public static Scene getCurrentScene() {
        return primaryStage != null ? primaryStage.getScene() : null;
    }
    
    /**
     * Clear navigation history
     */
    public static void clearHistory() {
        navigationStack.clear();
    }
    
    /**
     * Check if can go back
     */
    public static boolean canGoBack() {
        return !navigationStack.isEmpty();
    }
}
