package com.unframed.repository;

import com.unframed.entity.Follow;
import com.unframed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(
            User follower,
            User following
    );

    long countByFollowing(User user);

    List<Follow> findByFollower(User follower);
}