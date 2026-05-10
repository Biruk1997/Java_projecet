package com.blogapp.desktop.components;

import com.blogapp.desktop.models.Post;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.format.DateTimeFormatter;

/**
 * PostCard Component - Displays a post in card format
 * Equivalent to React's PostCard.jsx
 */
public class PostCard extends VBox {
    
    private Post post;
    private Runnable onClickHandler;
    
    public PostCard(Post post) {
        this.post = post;
        initializeUI();
    }
    
    public PostCard(Post post, Runnable onClickHandler) {
        this.post = post;
        this.onClickHandler = onClickHandler;
        initializeUI();
    }
    
    private void initializeUI() {
        // Card styling
        this.setPadding(new Insets(20));
        this.setSpacing(12);
        this.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3); " +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        this.setOnMouseEntered(e -> {
            this.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5); " +
                "-fx-cursor: hand;"
            );
        });
        
        this.setOnMouseExited(e -> {
            this.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3); " +
                "-fx-cursor: hand;"
            );
        });
        
        // Click handler - open post detail view
        this.setOnMouseClicked(e -> {
            if (onClickHandler != null) {
                onClickHandler.run();
            } else {
                // Default: open post detail view
                com.blogapp.desktop.views.PostDetailView.showInWindow(post);
            }
        });
        
        // Cover Image (if exists)
        if (post.getCoverImage() != null && !post.getCoverImage().isEmpty()) {
            ImageView coverImage = new ImageView();
            try {
                coverImage.setImage(new Image(post.getCoverImage(), true));
                coverImage.setFitWidth(350);
                coverImage.setFitHeight(200);
                coverImage.setPreserveRatio(true);
                coverImage.setStyle("-fx-background-radius: 8px;");
                this.getChildren().add(coverImage);
            } catch (Exception e) {
                // Image loading failed, skip
            }
        }
        
        // Author Info
        HBox authorBox = createAuthorBox();
        this.getChildren().add(authorBox);
        
        // Title
        Label titleLabel = new Label(post.getTitle());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(350);
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        this.getChildren().add(titleLabel);
        
        // Subtitle
        if (post.getSubtitle() != null && !post.getSubtitle().isEmpty()) {
            Label subtitleLabel = new Label(post.getSubtitle());
            subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
            subtitleLabel.setWrapText(true);
            subtitleLabel.setMaxWidth(350);
            subtitleLabel.setTextFill(Color.web("#666666"));
            this.getChildren().add(subtitleLabel);
        }
        
        // Excerpt (first 150 chars of content)
        String excerpt = getExcerpt(post.getContent(), 150);
        if (!excerpt.isEmpty()) {
            Label excerptLabel = new Label(excerpt);
            excerptLabel.setFont(Font.font("System", 13));
            excerptLabel.setWrapText(true);
            excerptLabel.setMaxWidth(350);
            excerptLabel.setTextFill(Color.web("#757575"));
            this.getChildren().add(excerptLabel);
        }
        
        // Topics
        if (post.getTopics() != null && !post.getTopics().isEmpty()) {
            HBox topicsBox = createTopicsBox();
            this.getChildren().add(topicsBox);
        }
        
        // Footer (stats)
        HBox footerBox = createFooterBox();
        this.getChildren().add(footerBox);
    }
    
    private HBox createAuthorBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        
        // Avatar placeholder
        Label avatarLabel = new Label(getInitials(post.getAuthor().getFirstname(), post.getAuthor().getLastname()));
        avatarLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-min-width: 40px; " +
            "-fx-min-height: 40px; " +
            "-fx-max-width: 40px; " +
            "-fx-max-height: 40px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold;"
        );
        
        // Author name and date
        VBox authorInfo = new VBox(2);
        Label authorName = new Label(post.getAuthor().getFirstname() + " " + post.getAuthor().getLastname());
        authorName.setFont(Font.font("System", FontWeight.BOLD, 13));
        authorName.setTextFill(Color.web("#1a1a1a"));
        
        String dateStr = post.getCreatedAt() != null ? 
            post.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "";
        Label dateLabel = new Label(dateStr);
        dateLabel.setFont(Font.font("System", 11));
        dateLabel.setTextFill(Color.web("#999999"));
        
        authorInfo.getChildren().addAll(authorName, dateLabel);
        
        box.getChildren().addAll(avatarLabel, authorInfo);
        return box;
    }
    
    private HBox createTopicsBox() {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        
        int maxTopics = Math.min(3, post.getTopics().size());
        for (int i = 0; i < maxTopics; i++) {
            Label topicLabel = new Label(post.getTopics().get(i).getName());
            topicLabel.setStyle(
                "-fx-background-color: #f0f0f0; " +
                "-fx-text-fill: #666666; " +
                "-fx-padding: 4px 12px; " +
                "-fx-background-radius: 12px; " +
                "-fx-font-size: 11px;"
            );
            box.getChildren().add(topicLabel);
        }
        
        return box;
    }
    
    private HBox createFooterBox() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        
        // Claps button (clickable)
        Label clapsLabel = new Label("👏 " + (post.getClapsCount() != null ? post.getClapsCount() : 0));
        clapsLabel.setFont(Font.font("System", 12));
        clapsLabel.setTextFill(Color.web("#666666"));
        clapsLabel.setStyle("-fx-cursor: hand; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        
        // Hover effect for claps
        clapsLabel.setOnMouseEntered(e -> {
            clapsLabel.setStyle("-fx-cursor: hand; -fx-padding: 5px 10px; -fx-background-color: #f0f0f0; -fx-background-radius: 5px;");
        });
        clapsLabel.setOnMouseExited(e -> {
            clapsLabel.setStyle("-fx-cursor: hand; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        });
        
        // Click handler for claps
        clapsLabel.setOnMouseClicked(e -> {
            e.consume(); // Prevent card click
            handleClapClick(clapsLabel);
        });
        
        // Comments button (clickable)
        Label commentsLabel = new Label("💬 " + (post.getCommentsCount() != null ? post.getCommentsCount() : 0));
        commentsLabel.setFont(Font.font("System", 12));
        commentsLabel.setTextFill(Color.web("#666666"));
        commentsLabel.setStyle("-fx-cursor: hand; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        
        // Hover effect for comments
        commentsLabel.setOnMouseEntered(e -> {
            commentsLabel.setStyle("-fx-cursor: hand; -fx-padding: 5px 10px; -fx-background-color: #f0f0f0; -fx-background-radius: 5px;");
        });
        commentsLabel.setOnMouseExited(e -> {
            commentsLabel.setStyle("-fx-cursor: hand; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        });
        
        // Click handler for comments
        commentsLabel.setOnMouseClicked(e -> {
            e.consume(); // Prevent card click
            if (onClickHandler != null) {
                onClickHandler.run(); // Open post detail to show comments
            }
        });
        
        // Reading time
        int readingTime = calculateReadingTime(post.getContent());
        Label readingTimeLabel = new Label("📖 " + readingTime + " min read");
        readingTimeLabel.setFont(Font.font("System", 12));
        readingTimeLabel.setTextFill(Color.web("#666666"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        box.getChildren().addAll(clapsLabel, commentsLabel, readingTimeLabel, spacer);
        return box;
    }
    
    private void handleClapClick(Label clapsLabel) {
        // Import required classes at the top of the file
        com.blogapp.desktop.services.ClapService.toggleClap(post.getPostId(), com.blogapp.desktop.utils.SessionManager.getToken())
            .thenAccept(response -> {
                javafx.application.Platform.runLater(() -> {
                    // Update the post claps count
                    post.setClapsCount(response.clapsCount);
                    // Update the label
                    clapsLabel.setText("👏 " + response.clapsCount);
                    // Change color if clapped
                    if (response.clapped) {
                        clapsLabel.setTextFill(Color.web("#667eea"));
                    } else {
                        clapsLabel.setTextFill(Color.web("#666666"));
                    }
                });
            })
            .exceptionally(error -> {
                javafx.application.Platform.runLater(() -> {
                    System.err.println("Failed to toggle clap: " + error.getMessage());
                });
                return null;
            });
    }
    
    private String getInitials(String firstname, String lastname) {
        String first = firstname != null && !firstname.isEmpty() ? firstname.substring(0, 1) : "";
        String last = lastname != null && !lastname.isEmpty() ? lastname.substring(0, 1) : "";
        return (first + last).toUpperCase();
    }
    
    private String getExcerpt(String content, int maxLength) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        
        // Strip HTML tags
        String text = content.replaceAll("<[^>]*>", "");
        
        if (text.length() <= maxLength) {
            return text;
        }
        
        return text.substring(0, maxLength) + "...";
    }
    
    private int calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 1;
        }
        
        String text = content.replaceAll("<[^>]*>", "");
        int wordCount = text.split("\\s+").length;
        int minutes = Math.max(1, wordCount / 200); // 200 words per minute
        return minutes;
    }
    
    public Post getPost() {
        return post;
    }
}
