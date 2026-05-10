package com.blogapp.repository;

import com.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    
    @Query("SELECT COUNT(f) FROM Follower f WHERE f.following.userId = :userId")
    Integer countFollowers(UUID userId);
    
    @Query("SELECT COUNT(f) FROM Follower f WHERE f.follower.userId = :userId")
    Integer countFollowing(UUID userId);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE :query OR LOWER(u.firstname) LIKE :query OR LOWER(u.lastname) LIKE :query OR LOWER(u.displayName) LIKE :query")
    List<User> searchByNameOrUsername(@Param("query") String query);
}
