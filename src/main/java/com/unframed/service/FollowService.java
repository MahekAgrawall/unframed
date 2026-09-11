package com.unframed.service;

import com.unframed.entity.Follow;
import com.unframed.entity.User;
import com.unframed.exception.UserNotFoundException;
import com.unframed.repository.FollowRepository;
import com.unframed.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(
            FollowRepository followRepository,
            UserRepository userRepository) {

        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void toggleFollow(Long targetUserId, Jwt jwt) {

        Long followerId = Long.valueOf(jwt.getSubject());

        User follower = userRepository.findById(followerId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        User following = userRepository.findById(targetUserId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (followerId.equals(targetUserId)) {
            throw new IllegalArgumentException(
                    "You cannot follow yourself");
        }

        var existingFollow =
                followRepository.findByFollowerAndFollowing(
                        follower,
                        following
                );

        if (existingFollow.isPresent()) {
            followRepository.delete(existingFollow.get());
        } else {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followRepository.save(follow);
        }
    }
}
