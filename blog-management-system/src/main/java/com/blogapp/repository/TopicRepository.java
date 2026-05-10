package com.blogapp.repository;

import com.blogapp.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    Optional<Topic> findBySlug(String slug);
    Optional<Topic> findByName(String name);
    Boolean existsBySlug(String slug);
    Boolean existsByName(String name);
    
    @Query("SELECT t FROM Topic t ORDER BY t.postsCount DESC, t.followersCount DESC")
    List<Topic> findTrendingTopics();
}
