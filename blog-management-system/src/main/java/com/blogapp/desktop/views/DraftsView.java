package com.blogapp.desktop.views;

import com.blogapp.desktop.models.Post;
import com.blogapp.desktop.services.PostService;
import com.blogapp.desktop.utils.SessionManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Drafts View - Shows user's draft posts
 */
public class DraftsView extends BorderPane {
    
    private VBox draftsContainer;
    private ProgressIndicator loadingIndicator;
    
    public DraftsView() {
        initializeUI();
        loadDrafts();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");
        
        // Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox contentBox = new VBox(30);
        contentBox.setPadding(new Insets(40, 60, 40, 60));
        contentBox.setMaxWidth(900);
        
        // Center wrapper
        HBox centerWrapper = new HBox(contentBox);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        
        // Header with gradient background
        VBox header = new VBox(10);
        header.setPadding(new Insets(40, 40, 40, 40));
        header.setStyle(
            "-fx-background-color: linear-gradient(135deg, #f59e0b 0%, #d97706 100%); " +
            "-fx-background-radius: 15px 15px 0 0;"
        );
        
        Label titleLabel = new Label("My Drafts");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Continue writing your stories");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#fff7ed"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);
        loadingIndicator.setVisible(true);
        
        HBox loadingBox = new HBox(loadingIndicator);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(60));
        
        // Drafts container
        draftsContainer = new VBox(20);
        draftsContainer.setMaxWidth(900);
        
        contentBox.getChildren().addAll(header, loadingBox, draftsContainer);
        
        scrollPane.setContent(centerWrapper);
        this.setCenter(scrollPane);
    }
    
    private void loadDrafts() {
        PostService.getUserDrafts(SessionManager.getToken())
            .thenAccept(posts -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    displayDrafts(posts);
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    showError("Failed to load drafts: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void displayDrafts(List<Post> drafts) {
        draftsContainer.getChildren().clear();
        
        if (drafts.isEmpty()) {
            Label emptyLabel = new Label("No drafts yet. Start writing!");
            emptyLabel.setFont(Font.font("System", 16));
            emptyLabel.setTextFill(Color.web("#999999"));
            emptyLabel.setPadding(new Insets(40));
            
            HBox emptyBox = new HBox(emptyLabel);
            emptyBox.setAlignment(Pos.CENTER);
            draftsContainer.getChildren().add(emptyBox);
            return;
        }
        
        for (Post draft : drafts) {
            VBox draftCard = createDraftCard(draft);
            draftsContainer.getChildren().add(draftCard);
        }
    }
    
    private VBox createDraftCard(Post draft) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3); -fx-cursor: hand;");
        
        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5); -fx-cursor: hand;");
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3); -fx-cursor: hand;");
        });
        
        // Title
        Label titleLabel = new Label(draft.getTitle() != null && !draft.getTitle().isEmpty() ? draft.getTitle() : "Untitled Draft");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        titleLabel.setWrapText(true);
        
        // Date
        String dateStr = draft.getUpdatedAt() != null ? 
            "Last edited: " + draft.getUpdatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) : "";
        Label dateLabel = new Label(dateStr);
        dateLabel.setFont(Font.font("System", 12));
        dateLabel.setTextFill(Color.web("#999999"));
        
        // Status badge
        Label statusLabel = new Label("📝 DRAFT");
        statusLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%); " +
            "-fx-text-fill: #92400e; " +
            "-fx-padding: 6px 16px; " +
            "-fx-background-radius: 15px; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold;"
        );
        
        HBox statusBox = new HBox(statusLabel);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        
        card.getChildren().addAll(titleLabel, dateLabel, statusBox);
        
        // Click to edit (placeholder)
        card.setOnMouseClicked(e -> {
            showInfo("Edit Draft", "Draft editing will be available soon!");
        });
        
        return card;
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void showInWindow() {
        Stage stage = new Stage();
        stage.setTitle("My Drafts");
        
        DraftsView view = new DraftsView();
        Scene scene = new Scene(view, 900, 700);
        stage.setScene(scene);
        stage.show();
    }
}
