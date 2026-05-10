package com.blogapp.repository;

import com.blogapp.entity.TopicFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicFollowerRepository extends JpaRepository<TopicFollower, TopicFollower.TopicFollowerId> {
    
    @Query("SELECT tf FROM TopicFollower tf WHERE tf.user.userId = :userId AND tf.topic.topicId = :topicId")
    Optional<TopicFollower> findByUserIdAndTopicId(UUID userId, UUID topicId);
    
    @Query("SELECT tf FROM TopicFollower tf WHERE tf.user.userId = :userId")
    List<TopicFollower> findByUserId(UUID userId);
    
    @Query("SELECT CASE WHEN COUNT(tf) > 0 THEN true ELSE false END FROM TopicFollower tf WHERE tf.user.userId = :userId AND tf.topic.topicId = :topicId")
    Boolean existsByUserIdAndTopicId(UUID userId, UUID topicId);
}
