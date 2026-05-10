package com.blogapp.repository;

import com.blogapp.entity.Follower;
import com.blogapp.entity.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {
    
    @Query("SELECT f FROM Follower f WHERE f.follower.userId = :followerId AND f.following.userId = :followingId")
    Optional<Follower> findByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
    
    @Query("SELECT f FROM Follower f WHERE f.following.userId = :userId")
    List<Follower> findFollowersByUserId(UUID userId);
    
    @Query("SELECT f FROM Follower f WHERE f.follower.userId = :userId")
    List<Follower> findFollowingByUserId(UUID userId);
    
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Follower f WHERE f.follower.userId = :followerId AND f.following.userId = :followingId")
    Boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
}
