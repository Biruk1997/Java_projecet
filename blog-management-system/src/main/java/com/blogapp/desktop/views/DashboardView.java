package com.blogapp.desktop.views;

import com.blogapp.desktop.components.*;
import com.blogapp.desktop.models.Post;
import com.blogapp.desktop.models.User;
import com.blogapp.desktop.services.PostService;
import com.blogapp.desktop.services.TopicService;
import com.blogapp.desktop.utils.ErrorHandler;
import com.blogapp.desktop.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
/**
 * DashboardView - Main user dashboard with feed
 * Equivalent to React's Dashboard.jsx
 */
public class DashboardView extends BorderPane {
    private Stage stage;
    private String token;
    private String userId;
    private String username;
    private VBox postsContainer;
    private ProgressIndicator loadingIndicator;
    private List<Post> posts = new ArrayList<>();
    
    public DashboardView(Stage stage) {
        this.stage = stage;
        this.token = SessionManager.getToken();
        this.userId = SessionManager.getUserId();
        this.username = SessionManager.getUsername();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        // Top Navigation
        NavigationBar navbar = new NavigationBar(username != null ? username : "User");
        navbar.setOnWrite(() -> showCreatePost());
        navbar.setOnProfile(() -> showProfile());
        navbar.setOnLogout(() -> handleLogout());
        navbar.setOnSearch(() -> showSearch());
        this.setTop(navbar);
        
        // Left Sidebar
        Sidebar sidebar = new Sidebar(username != null ? username : "User");
        sidebar.setOnHome(() -> loadData());
        sidebar.setOnWrite(() -> showCreatePost());
        sidebar.setOnDrafts(() -> showDrafts());
        sidebar.setOnBookmarks(() -> showBookmarks());
        sidebar.setOnFollowers(() -> showFollowers());
        sidebar.setOnFollowing(() -> showFollowing());
        sidebar.setOnDiscover(() -> showDiscoverUsers());
        sidebar.setOnProfile(() -> showProfile());
        sidebar.setOnSettings(() -> showSettings());
        this.setLeft(sidebar);
        
        // Center Content
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(30));
        centerContent.setStyle("-fx-background-color: #f5f5f5;");
        
        // Welcome Banner
        VBox banner = createWelcomeBanner();
        centerContent.getChildren().add(banner);
        
        // Feed Tabs
        HBox tabsBox = createFeedTabs();
        centerContent.getChildren().add(tabsBox);
        
        // Posts Container
        postsContainer = new VBox(20);
        
        // Loading Indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);
        VBox loadingBox = new VBox(loadingIndicator);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(50));
        postsContainer.getChildren().add(loadingBox);
        
        ScrollPane scrollPane = new ScrollPane(postsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-background-color: #f5f5f5;");
        
        centerContent.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        this.setCenter(centerContent);
        
        // Right Sidebar (Trending Topics)
        VBox rightSidebar = createRightSidebar();
        this.setRight(rightSidebar);
    }
    
    private VBox createWelcomeBanner() {
        VBox banner = new VBox(15);
        banner.setPadding(new Insets(30));
        banner.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-background-radius: 15px;"
        );
        
        Label welcomeLabel = new Label("Welcome back, " + (username != null ? username : "User") + "! 👋");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Continue your writing journey");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#e0e0ff"));
        
        Button writeButton = new Button("✍️ Write New Story");
        writeButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 12px 24px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        writeButton.setOnAction(e -> showCreatePost());
        
        banner.getChildren().addAll(welcomeLabel, subtitleLabel, writeButton);
        return banner;
    }
    
    private HBox createFeedTabs() {
        HBox tabsBox = new HBox();
        tabsBox.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );
        
        Button forYouTab = new Button("📊 For You");
        Button followingTab = new Button("👥 Following");
        
        String activeStyle = 
            "-fx-background-color: #f0f0ff; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15px 30px; " +
            "-fx-background-radius: 10px; " +
            "-fx-cursor: hand;";
        
        String inactiveStyle = 
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #666666; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 15px 30px; " +
            "-fx-background-radius: 10px; " +
            "-fx-cursor: hand;";
        
        forYouTab.setStyle(activeStyle);
        followingTab.setStyle(inactiveStyle);
        
        forYouTab.setMaxWidth(Double.MAX_VALUE);
        followingTab.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(forYouTab, Priority.ALWAYS);
        HBox.setHgrow(followingTab, Priority.ALWAYS);
        
        forYouTab.setOnAction(e -> {
            forYouTab.setStyle(activeStyle);
            followingTab.setStyle(inactiveStyle);
            loadData();
        });
        
        followingTab.setOnAction(e -> {
            followingTab.setStyle(activeStyle);
            forYouTab.setStyle(inactiveStyle);
            loadFollowingFeed();
        });
        
        tabsBox.getChildren().addAll(forYouTab, followingTab);
        return tabsBox;
    }
    
    private VBox createRightSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(300);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 0 1px;");
        
        Label trendingLabel = new Label("🔥 Trending Topics");
        trendingLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        VBox topicsBox = new VBox(10);
        // Load trending topics
        TopicService.getTrendingTopics(token)
            .thenAccept(topics -> {
                javafx.application.Platform.runLater(() -> {
                    for (int i = 0; i < Math.min(5, topics.size()); i++) {
                        var topic = topics.get(i);
                        TopicChip chip = new TopicChip(topic, true, false);
                        topicsBox.getChildren().add(chip);
                    }
                });
            })
            .exceptionally(error -> {
                System.err.println("Failed to load trending topics: " + error.getMessage());
                return null;
            });
        
        sidebar.getChildren().addAll(trendingLabel, topicsBox);
        return sidebar;
    }
    
    private void loadData() {
        postsContainer.getChildren().clear();
        loadingIndicator.setVisible(true);
        VBox loadingBox = new VBox(loadingIndicator);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(50));
        postsContainer.getChildren().add(loadingBox);
        
        PostService.getAllPosts(token)
            .thenAccept(loadedPosts -> {
                javafx.application.Platform.runLater(() -> {
                    posts = loadedPosts;
                    displayPosts();
                });
            })
            .exceptionally(error -> {
                javafx.application.Platform.runLater(() -> {
                    postsContainer.getChildren().clear();
                    EmptyState emptyState = new EmptyState(
                        "❌",
                        "Failed to load posts",
                        "Please try again later",
                        null
                    );
                    postsContainer.getChildren().add(emptyState);
                });
                return null;
            });
    }
    
    private void loadFollowingFeed() {
        postsContainer.getChildren().clear();
        loadingIndicator.setVisible(true);
        VBox loadingBox = new VBox(loadingIndicator);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(50));
        postsContainer.getChildren().add(loadingBox);
        
        PostService.getFeed(token)
            .thenAccept(loadedPosts -> {
                javafx.application.Platform.runLater(() -> {
                    posts = loadedPosts;
                    displayPosts();
                });
            })
            .exceptionally(error -> {
                javafx.application.Platform.runLater(() -> {
                    loadData(); // Fallback to all posts
                });
                return null;
            });
    }
    
    private void displayPosts() {
        postsContainer.getChildren().clear();
        
        if (posts == null || posts.isEmpty()) {
            EmptyState emptyState = new EmptyState(
                "📭",
                "No posts yet",
                "Be the first to write a story!",
                () -> showCreatePost()
            );
            postsContainer.getChildren().add(emptyState);
            return;
        }
        
        for (Post post : posts) {
            PostCard card = new PostCard(post, () -> showPostDetail(post));
            postsContainer.getChildren().add(card);
        }
    }
    
    private void showCreatePost() {
        CreatePostView createPostView = new CreatePostView(stage);
        stage.setScene(createPostView.createScene());
        stage.setTitle("Write Story - Blog Platform");
    }
    
    private void showPostDetail(Post post) {
        PostDetailView.showInWindow(post);
    }
    
    private void showProfile() {
        // Create a User object from session data
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setEmail(SessionManager.getEmail());
        user.setDisplayName(username);
        
        ProfileView.showInWindow(user);
    }
    
    private void showDrafts() {
        DraftsView.showInWindow();
    }
    
    private void showBookmarks() {
        BookmarksView.showInWindow();
    }
    
    private void showFollowers() {
        FollowManagementView.showInWindow(userId);
    }
    
    private void showFollowing() {
        FollowManagementView.showInWindow(userId);
    }
    
    private void showDiscoverUsers() {
        DiscoverUsersView.showInWindow();
    }
    
    private void showSettings() {
        SettingsView.showInWindow();
    }
    
    private void showSearch() {
        // TODO: Implement SearchView
        ErrorHandler.showInfo("Coming Soon", "Search view will be available soon!");
    }
    
    private void handleLogout() {
        SessionManager.clearSession();
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.createScene());
        stage.setTitle("Login - Blog Platform");
    }
    
    public Scene createScene() {
        return new Scene(this, 1400, 900);
    }
}
