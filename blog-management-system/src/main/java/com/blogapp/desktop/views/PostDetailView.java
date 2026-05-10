package com.blogapp.desktop.views;

import com.blogapp.desktop.models.Comment;
import com.blogapp.desktop.models.Post;
import com.blogapp.desktop.models.User;
import com.blogapp.desktop.services.ClapService;
import com.blogapp.desktop.services.CommentService;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Post Detail View - Shows full post content with comments
 */
public class PostDetailView extends BorderPane {
    
    private Post post;
    private VBox commentsContainer;
    private TextArea commentInput;
    private Label clapsLabel;
    private Label commentsCountLabel;
    
    public PostDetailView(Post post) {
        this.post = post;
        initializeUI();
        loadComments();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");
        
        // Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox contentBox = new VBox(25);
        contentBox.setPadding(new Insets(50, 80, 50, 80));
        contentBox.setMaxWidth(900);
        contentBox.setStyle("-fx-alignment: center;");
        
        // Center the content box
        HBox centerWrapper = new HBox(contentBox);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        
        // Article card wrapper
        VBox articleCard = new VBox(30);
        articleCard.setPadding(new Insets(50));
        articleCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 25, 0, 0, 5);"
        );
        
        // Title
        Label titleLabel = new Label(post.getTitle());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 38));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(800);
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        articleCard.getChildren().add(titleLabel);
        
        // Subtitle
        if (post.getSubtitle() != null && !post.getSubtitle().isEmpty()) {
            Label subtitleLabel = new Label(post.getSubtitle());
            subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 20));
            subtitleLabel.setWrapText(true);
            subtitleLabel.setMaxWidth(800);
            subtitleLabel.setTextFill(Color.web("#666666"));
            articleCard.getChildren().add(subtitleLabel);
        }
        
        // Author info
        HBox authorBox = createAuthorBox();
        articleCard.getChildren().add(authorBox);
        
        // Topics
        if (post.getTopics() != null && !post.getTopics().isEmpty()) {
            HBox topicsBox = createTopicsBox();
            articleCard.getChildren().add(topicsBox);
        }
        
        // Separator
        Separator separator1 = new Separator();
        separator1.setMaxWidth(800);
        separator1.setStyle("-fx-background-color: #e0e0e0;");
        articleCard.getChildren().add(separator1);
        
        // Content (HTML rendered)
        WebView contentView = new WebView();
        contentView.getEngine().loadContent(wrapHtmlContent(post.getContent()));
        contentView.setPrefHeight(600);
        contentView.setMaxWidth(800);
        articleCard.getChildren().add(contentView);
        
        // Separator
        Separator separator2 = new Separator();
        separator2.setMaxWidth(800);
        separator2.setStyle("-fx-background-color: #e0e0e0;");
        articleCard.getChildren().add(separator2);
        
        // Claps and Comments stats
        HBox statsBox = createStatsBox();
        articleCard.getChildren().add(statsBox);
        
        contentBox.getChildren().add(articleCard);
        
        // Comments section (separate card)
        VBox commentsCard = new VBox(25);
        commentsCard.setPadding(new Insets(40));
        commentsCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 25, 0, 0, 5);"
        );
        
        Label commentsTitle = new Label("💬 Comments");
        commentsTitle.setFont(Font.font("System", FontWeight.BOLD, 28));
        commentsTitle.setTextFill(Color.web("#1a1a1a"));
        commentsCard.getChildren().add(commentsTitle);
        
        // Comment input
        VBox commentInputBox = createCommentInputBox();
        commentsCard.getChildren().add(commentInputBox);
        
        // Comments list
        commentsContainer = new VBox(20);
        commentsContainer.setMaxWidth(800);
        commentsCard.getChildren().add(commentsContainer);
        
        contentBox.getChildren().add(commentsCard);
        
        scrollPane.setContent(centerWrapper);
        this.setCenter(scrollPane);
    }
    
    private HBox createAuthorBox() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10, 0, 10, 0));
        
        // Avatar
        String initials = getInitials(post.getAuthor());
        Label avatarLabel = new Label(initials);
        avatarLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 35px; " +
            "-fx-min-width: 70px; " +
            "-fx-min-height: 70px; " +
            "-fx-max-width: 70px; " +
            "-fx-max-height: 70px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 24px; " +
            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 10, 0, 0, 3);"
        );
        
        // Author info
        VBox authorInfo = new VBox(5);
        Label authorName = new Label(post.getAuthor().getFullName());
        authorName.setFont(Font.font("System", FontWeight.BOLD, 16));
        authorName.setTextFill(Color.web("#1a1a1a"));
        
        String dateStr = post.getCreatedAt() != null ? 
            post.getCreatedAt().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) : "";
        Label dateLabel = new Label(dateStr);
        dateLabel.setFont(Font.font("System", 13));
        dateLabel.setTextFill(Color.web("#999999"));
        
        authorInfo.getChildren().addAll(authorName, dateLabel);
        
        box.getChildren().addAll(avatarLabel, authorInfo);
        return box;
    }
    
    private HBox createTopicsBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        
        for (int i = 0; i < post.getTopics().size(); i++) {
            Label topicLabel = new Label(post.getTopics().get(i).getName());
            topicLabel.setStyle(
                "-fx-background-color: #e8f0fe; " +
                "-fx-text-fill: #1967d2; " +
                "-fx-padding: 6px 16px; " +
                "-fx-background-radius: 16px; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold;"
            );
            box.getChildren().add(topicLabel);
        }
        
        return box;
    }
    
    private HBox createStatsBox() {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);
        
        // Claps button
        clapsLabel = new Label("👏 " + (post.getClapsCount() != null ? post.getClapsCount() : 0));
        clapsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        clapsLabel.setTextFill(Color.web("#666666"));
        clapsLabel.setStyle("-fx-cursor: hand; -fx-padding: 8px 16px; -fx-background-radius: 8px;");
        
        clapsLabel.setOnMouseEntered(e -> {
            clapsLabel.setStyle("-fx-cursor: hand; -fx-padding: 8px 16px; -fx-background-color: #f0f0f0; -fx-background-radius: 8px;");
        });
        clapsLabel.setOnMouseExited(e -> {
            clapsLabel.setStyle("-fx-cursor: hand; -fx-padding: 8px 16px; -fx-background-radius: 8px;");
        });
        
        clapsLabel.setOnMouseClicked(e -> handleClapClick());
        
        // Comments count
        commentsCountLabel = new Label("💬 " + (post.getCommentsCount() != null ? post.getCommentsCount() : 0));
        commentsCountLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        commentsCountLabel.setTextFill(Color.web("#666666"));
        
        box.getChildren().addAll(clapsLabel, commentsCountLabel);
        return box;
    }
    
    private VBox createCommentInputBox() {
        VBox box = new VBox(12);
        box.setMaxWidth(800);
        box.setPadding(new Insets(20));
        box.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px;"
        );
        
        Label label = new Label("Add a comment");
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#1a1a1a"));
        
        commentInput = new TextArea();
        commentInput.setPromptText("Share your thoughts...");
        commentInput.setPrefRowCount(3);
        commentInput.setWrapText(true);
        commentInput.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 4px; " +
            "-fx-background-radius: 4px;"
        );
        
        Button submitButton = new Button("Post Comment");
        submitButton.setStyle(
            "-fx-background-color: #667eea; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 24px; " +
            "-fx-background-radius: 6px; " +
            "-fx-cursor: hand;"
        );
        
        submitButton.setOnMouseEntered(e -> {
            submitButton.setStyle(
                "-fx-background-color: #5568d3; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 24px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;"
            );
        });
        
        submitButton.setOnMouseExited(e -> {
            submitButton.setStyle(
                "-fx-background-color: #667eea; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 24px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;"
            );
        });
        
        submitButton.setOnAction(e -> handleSubmitComment());
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(submitButton);
        
        box.getChildren().addAll(label, commentInput, buttonBox);
        return box;
    }
    
    private void handleClapClick() {
        ClapService.toggleClap(post.getPostId(), SessionManager.getToken())
            .thenAccept(response -> {
                Platform.runLater(() -> {
                    post.setClapsCount(response.clapsCount);
                    clapsLabel.setText("👏 " + response.clapsCount);
                    if (response.clapped) {
                        clapsLabel.setTextFill(Color.web("#667eea"));
                    } else {
                        clapsLabel.setTextFill(Color.web("#666666"));
                    }
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    showError("Failed to toggle clap: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void handleSubmitComment() {
        String content = commentInput.getText().trim();
        
        if (content.isEmpty()) {
            showError("Please enter a comment");
            return;
        }
        
        CommentService.addComment(post.getPostId(), content, SessionManager.getToken())
            .thenAccept(comment -> {
                Platform.runLater(() -> {
                    commentInput.clear();
                    // Update comments count
                    int currentCount = post.getCommentsCount() != null ? post.getCommentsCount() : 0;
                    post.setCommentsCount(currentCount + 1);
                    commentsCountLabel.setText("💬 " + post.getCommentsCount());
                    // Reload comments
                    loadComments();
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    showError("Failed to post comment: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void loadComments() {
        CommentService.getComments(post.getPostId(), SessionManager.getToken())
            .thenAccept(comments -> {
                Platform.runLater(() -> {
                    displayComments(comments);
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    System.err.println("Failed to load comments: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void displayComments(List<Comment> comments) {
        commentsContainer.getChildren().clear();
        
        if (comments.isEmpty()) {
            Label noComments = new Label("No comments yet. Be the first to comment!");
            noComments.setFont(Font.font("System", 14));
            noComments.setTextFill(Color.web("#999999"));
            commentsContainer.getChildren().add(noComments);
            return;
        }
        
        for (Comment comment : comments) {
            VBox commentBox = createCommentBox(comment);
            commentsContainer.getChildren().add(commentBox);
        }
    }
    
    private VBox createCommentBox(Comment comment) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px;"
        );
        
        // Author info
        HBox authorBox = new HBox(10);
        authorBox.setAlignment(Pos.CENTER_LEFT);
        
        String initials = getInitials(comment.getAuthor());
        Label avatarLabel = new Label(initials);
        avatarLabel.setStyle(
            "-fx-background-color: #667eea; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-min-width: 40px; " +
            "-fx-min-height: 40px; " +
            "-fx-max-width: 40px; " +
            "-fx-max-height: 40px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold;"
        );
        
        VBox authorInfo = new VBox(2);
        Label authorName = new Label(comment.getAuthor().getFullName());
        authorName.setFont(Font.font("System", FontWeight.BOLD, 14));
        authorName.setTextFill(Color.web("#1a1a1a"));
        
        String dateStr = comment.getCreatedAt() != null ? 
            comment.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")) : "";
        Label dateLabel = new Label(dateStr);
        dateLabel.setFont(Font.font("System", 11));
        dateLabel.setTextFill(Color.web("#999999"));
        
        authorInfo.getChildren().addAll(authorName, dateLabel);
        authorBox.getChildren().addAll(avatarLabel, authorInfo);
        
        // Comment content
        Label contentLabel = new Label(comment.getContent());
        contentLabel.setFont(Font.font("System", 14));
        contentLabel.setWrapText(true);
        contentLabel.setTextFill(Color.web("#333333"));
        
        box.getChildren().addAll(authorBox, contentLabel);
        return box;
    }
    
    private String getInitials(User user) {
        if (user.getFirstname() != null && !user.getFirstname().isEmpty() && 
            user.getLastname() != null && !user.getLastname().isEmpty()) {
            return (user.getFirstname().substring(0, 1) + user.getLastname().substring(0, 1)).toUpperCase();
        }
        if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            String[] parts = user.getDisplayName().split(" ");
            if (parts.length >= 2) {
                return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
            }
            return user.getDisplayName().substring(0, Math.min(2, user.getDisplayName().length())).toUpperCase();
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            return user.getUsername().substring(0, Math.min(2, user.getUsername().length())).toUpperCase();
        }
        return "??";
    }
    
    private String wrapHtmlContent(String content) {
        return "<html><head><style>" +
               "body { font-family: 'Segoe UI', Arial, sans-serif; font-size: 18px; line-height: 1.8; color: #333; padding: 20px; }" +
               "p { margin: 16px 0; }" +
               "h1, h2, h3 { margin: 24px 0 16px 0; color: #1a1a1a; }" +
               "img { max-width: 100%; height: auto; }" +
               "code { background-color: #f5f5f5; padding: 2px 6px; border-radius: 3px; }" +
               "pre { background-color: #f5f5f5; padding: 16px; border-radius: 6px; overflow-x: auto; }" +
               "</style></head><body>" + content + "</body></html>";
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void showInWindow(Post post) {
        Stage stage = new Stage();
        stage.setTitle(post.getTitle());
        
        PostDetailView view = new PostDetailView(post);
        Scene scene = new Scene(view, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }
}
