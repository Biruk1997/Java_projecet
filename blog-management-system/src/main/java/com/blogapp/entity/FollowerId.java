package com.blogapp.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Composite key for Follower entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FollowerId implements Serializable {
    private UUID followerId;
    private UUID followingId;
}
