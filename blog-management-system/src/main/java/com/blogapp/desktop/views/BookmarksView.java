package com.blogapp.desktop.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Bookmarks View - Shows user's bookmarked posts
 */
public class BookmarksView extends BorderPane {
    
    public BookmarksView() {
        initializeUI();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");
        
        VBox contentBox = new VBox(40);
        contentBox.setPadding(new Insets(80));
        contentBox.setAlignment(Pos.CENTER);
        
        // Header with gradient background
        VBox header = new VBox(15);
        header.setPadding(new Insets(40));
        header.setAlignment(Pos.CENTER);
        header.setMaxWidth(600);
        header.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-background-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 20, 0, 0, 5);"
        );
        
        Label titleLabel = new Label("My Bookmarks");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);
        
        header.getChildren().add(titleLabel);
        
        // Empty state card
        VBox emptyCard = new VBox(25);
        emptyCard.setPadding(new Insets(60));
        emptyCard.setAlignment(Pos.CENTER);
        emptyCard.setMaxWidth(500);
        emptyCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 4);"
        );
        
        // Empty state
        Label emptyLabel = new Label("📚");
        emptyLabel.setFont(Font.font("System", 72));
        
        Label messageLabel = new Label("No bookmarks yet");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        messageLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label subLabel = new Label("Save posts you want to read later by clicking the bookmark icon");
        subLabel.setFont(Font.font("System", 15));
        subLabel.setTextFill(Color.web("#666666"));
        subLabel.setWrapText(true);
        subLabel.setMaxWidth(400);
        subLabel.setAlignment(Pos.CENTER);
        subLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        emptyCard.getChildren().addAll(emptyLabel, messageLabel, subLabel);
        
        contentBox.getChildren().addAll(header, emptyCard);
        
        this.setCenter(contentBox);
    }
    
    public static void showInWindow() {
        Stage stage = new Stage();
        stage.setTitle("My Bookmarks");
        
        BookmarksView view = new BookmarksView();
        Scene scene = new Scene(view, 900, 700);
        stage.setScene(scene);
        stage.show();
    }
}
