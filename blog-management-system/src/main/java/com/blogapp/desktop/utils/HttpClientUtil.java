package com.blogapp.desktop.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP Client utility for making API calls to Spring Boot backend
 */
public class HttpClientUtil {
    
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * GET request
     */
    public static <T> CompletableFuture<T> get(String endpoint, String token, Class<T> responseType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + endpoint))
                        .GET()
                        .timeout(Duration.ofSeconds(30));
                
                if (token != null && !token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }
                
                HttpRequest request = requestBuilder.build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    return objectMapper.readValue(response.body(), responseType);
                } else {
                    throw new RuntimeException("HTTP " + response.statusCode() + ": " + response.body());
                }
            } catch (Exception e) {
                throw new RuntimeException("GET request failed: " + e.getMessage(), e);
            }
        });
    }
    
    /**
     * POST request
     */
    public static <T> CompletableFuture<T> post(String endpoint, Object body, String token, Class<T> responseType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonBody = objectMapper.writeValueAsString(body);
                
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + endpoint))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(30));
                
                if (token != null && !token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }
                
                HttpRequest request = requestBuilder.build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    if (response.body() == null || response.body().isEmpty()) {
                        return null;
                    }
                    return objectMapper.readValue(response.body(), responseType);
                } else {
                    throw new RuntimeException("HTTP " + response.statusCode() + ": " + response.body());
                }
            } catch (Exception e) {
                throw new RuntimeException("POST request failed: " + e.getMessage(), e);
            }
        });
    }
    
    /**
     * PUT request
     */
    public static <T> CompletableFuture<T> put(String endpoint, Object body, String token, Class<T> responseType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonBody = objectMapper.writeValueAsString(body);
                
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + endpoint))
                        .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(30));
                
                if (token != null && !token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }
                
                HttpRequest request = requestBuilder.build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    if (response.body() == null || response.body().isEmpty()) {
                        return null;
                    }
                    return objectMapper.readValue(response.body(), responseType);
                } else {
                    throw new RuntimeException("HTTP " + response.statusCode() + ": " + response.body());
                }
            } catch (Exception e) {
                throw new RuntimeException("PUT request failed: " + e.getMessage(), e);
            }
        });
    }
    
    /**
     * DELETE request
     */
    public static CompletableFuture<Void> delete(String endpoint, String token) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + endpoint))
                        .DELETE()
                        .timeout(Duration.ofSeconds(30));
                
                if (token != null && !token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }
                
                HttpRequest request = requestBuilder.build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    throw new RuntimeException("HTTP " + response.statusCode() + ": " + response.body());
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException("DELETE request failed: " + e.getMessage(), e);
            }
        });
    }
    
    /**
     * Synchronous GET request
     */
    public static <T> T getSync(String endpoint, String token, Class<T> responseType) {
        try {
            return get(endpoint, token, responseType).get();
        } catch (Exception e) {
            throw new RuntimeException("Sync GET failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Synchronous POST request
     */
    public static <T> T postSync(String endpoint, Object body, String token, Class<T> responseType) {
        try {
            return post(endpoint, body, token, responseType).get();
        } catch (Exception e) {
            throw new RuntimeException("Sync POST failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Synchronous PUT request
     */
    public static <T> T putSync(String endpoint, Object body, String token, Class<T> responseType) {
        try {
            return put(endpoint, body, token, responseType).get();
        } catch (Exception e) {
            throw new RuntimeException("Sync PUT failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Synchronous DELETE request
     */
    public static void deleteSync(String endpoint, String token) {
        try {
            delete(endpoint, token).get();
        } catch (Exception e) {
            throw new RuntimeException("Sync DELETE failed: " + e.getMessage(), e);
        }
    }
}
