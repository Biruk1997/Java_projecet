package com.blogapp.repository;

import com.blogapp.entity.Clap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClapRepository extends JpaRepository<Clap, UUID> {
    Optional<Clap> findByPostPostIdAndUserUserId(UUID postId, UUID userId);
    
    @Query("SELECT SUM(c.count) FROM Clap c WHERE c.post.postId = :postId")
    Integer countClapsByPostId(UUID postId);
    
    Boolean existsByPostPostIdAndUserUserId(UUID postId, UUID userId);
}
