package com.blogapp.desktop.components;

import com.blogapp.desktop.models.Post;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * PostList Component - Container for displaying posts in grid or list layout
 * Equivalent to React's PostList.jsx
 */
public class PostList extends VBox {
    
    private FlowPane gridContainer;
    private VBox listContainer;
    private String layout = "grid"; // "grid" or "list"
    private List<Post> posts;
    
    public PostList(List<Post> posts, String layout) {
        this.posts = posts;
        this.layout = layout;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        
        if (posts == null || posts.isEmpty()) {
            // Show empty state
            EmptyState emptyState = new EmptyState(
                "📭",
                "No posts found",
                "Be the first to write a story!",
                null
            );
            this.getChildren().add(emptyState);
            return;
        }
        
        if ("grid".equals(layout)) {
            createGridLayout();
        } else {
            createListLayout();
        }
    }
    
    private void createGridLayout() {
        gridContainer = new FlowPane();
        gridContainer.setHgap(20);
        gridContainer.setVgap(20);
        gridContainer.setAlignment(Pos.TOP_LEFT);
        
        for (Post post : posts) {
            PostCard card = new PostCard(post);
            card.setPrefWidth(350);
            gridContainer.getChildren().add(card);
        }
        
        ScrollPane scrollPane = new ScrollPane(gridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        this.getChildren().add(scrollPane);
    }
    
    private void createListLayout() {
        listContainer = new VBox(15);
        listContainer.setPadding(new Insets(0));
        
        for (Post post : posts) {
            PostCard card = new PostCard(post);
            card.setMaxWidth(Double.MAX_VALUE);
            listContainer.getChildren().add(card);
        }
        
        ScrollPane scrollPane = new ScrollPane(listContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        this.getChildren().add(scrollPane);
    }
    
    public void setPosts(List<Post> posts) {
        this.posts = posts;
        this.getChildren().clear();
        initializeUI();
    }
    
    public void setLayout(String layout) {
        this.layout = layout;
        this.getChildren().clear();
        initializeUI();
    }
}
