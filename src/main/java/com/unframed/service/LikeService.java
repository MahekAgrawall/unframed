package com.unframed.service;

import com.unframed.entity.Artwork;
import com.unframed.entity.Like;
import com.unframed.entity.User;
import com.unframed.exception.UserNotFoundException;
import com.unframed.repository.ArtworkRepository;
import com.unframed.repository.LikeRepository;
import com.unframed.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    public LikeService(
            LikeRepository likeRepository,
            UserRepository userRepository,
            ArtworkRepository artworkRepository) {

        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    public void toggleLike(Long artworkId, Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() ->
                        new RuntimeException("Artwork not found"));

        var existingLike =
                likeRepository.findByUserAndArtwork(user, artwork);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setArtwork(artwork);
            likeRepository.save(like);
        }
    }

    public long getLikeCount(Long artworkId) {

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() ->
                        new RuntimeException("Artwork not found"));

        return likeRepository.countByArtwork(artwork);
    }
}