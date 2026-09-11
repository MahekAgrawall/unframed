package com.unframed.service;

import com.unframed.dto.ArtworkResponse;
import com.unframed.entity.Artwork;
import com.unframed.entity.User;
import com.unframed.repository.ArtworkRepository;
import com.unframed.repository.FollowRepository;
import com.unframed.repository.UserRepository;
import com.unframed.exception.UserNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ArtworkRepository artworkRepository;

    public FeedService(
            UserRepository userRepository,
            FollowRepository followRepository,
            ArtworkRepository artworkRepository) {

        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.artworkRepository = artworkRepository;
    }

    public List<ArtworkResponse> getFeed(Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        List<Long> userIds = new ArrayList<>();

        // Include the current user's own artworks
        userIds.add(currentUser.getId());

        // Include artworks from people they follow
        followRepository.findByFollower(currentUser)
                .forEach(follow ->
                        userIds.add(follow.getFollowing().getId())
                );

        return artworkRepository
                .findByUserIdInOrderByCreatedAtDesc(userIds)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ArtworkResponse mapToResponse(Artwork artwork) {

        ArtworkResponse response = new ArtworkResponse();

        response.setId(artwork.getId());
        response.setTitle(artwork.getTitle());
        response.setDescription(artwork.getDescription());
        response.setImageUrl(artwork.getImageUrl());
        response.setUserId(artwork.getUser().getId());
        response.setUserName(artwork.getUser().getName());
        response.setCreatedAt(artwork.getCreatedAt());

        return response;
    }
}