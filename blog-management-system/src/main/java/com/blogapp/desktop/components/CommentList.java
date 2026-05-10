package com.blogapp.desktop.components;

import com.blogapp.desktop.models.Comment;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * CommentList Component - Displays list of comments with nested replies
 * Equivalent to React's CommentList.jsx
 */
public class CommentList extends VBox {
    
    private List<Comment> comments;
    private String currentUserId;
    
    public CommentList(List<Comment> comments, String currentUserId) {
        this.comments = comments;
        this.currentUserId = currentUserId;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setSpacing(15);
        this.setPadding(new Insets(0));
        
        if (comments == null || comments.isEmpty()) {
            EmptyState emptyState = new EmptyState(
                "💬",
                "No comments yet",
                "Be the first to share your thoughts!",
                null
            );
            this.getChildren().add(emptyState);
            return;
        }
        
        VBox commentsContainer = new VBox(15);
        
        for (Comment comment : comments) {
            boolean isOwner = currentUserId != null && 
                            currentUserId.equals(comment.getAuthor().getUserId());
            
            CommentItem commentItem = new CommentItem(comment, isOwner);
            commentsContainer.getChildren().add(commentItem);
        }
        
        ScrollPane scrollPane = new ScrollPane(commentsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        this.getChildren().add(scrollPane);
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.getChildren().clear();
        initializeUI();
    }
    
    public void addComment(Comment comment) {
        if (this.comments != null) {
            this.comments.add(0, comment);
            this.getChildren().clear();
            initializeUI();
        }
    }
}
