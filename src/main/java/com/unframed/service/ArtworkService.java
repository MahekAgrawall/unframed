package com.unframed.service;

import com.unframed.dto.ArtworkRequest;
import com.unframed.dto.ArtworkResponse;
import com.unframed.entity.Artwork;
import com.unframed.entity.User;
import com.unframed.exception.UserNotFoundException;
import com.unframed.repository.ArtworkRepository;
import com.unframed.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public ArtworkService(
            ArtworkRepository artworkRepository,
            UserRepository userRepository) {

        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    public ArtworkResponse createArtwork(
            Jwt jwt,
            ArtworkRequest request) {

        Long userId = Long.valueOf(jwt.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        Artwork artwork = new Artwork();

        artwork.setTitle(request.getTitle());
        artwork.setDescription(request.getDescription());
        artwork.setImageUrl(request.getImageUrl());
        artwork.setUser(user);

        Artwork savedArtwork = artworkRepository.save(artwork);

        return mapToResponse(savedArtwork);
    }

    public List<ArtworkResponse> getAllArtworks() {

        return artworkRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ArtworkResponse getArtworkById(Long id) {

        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Artwork not found")
                );

        return mapToResponse(artwork);
    }

    public ArtworkResponse updateArtwork(
            Long id,
            Jwt jwt,
            ArtworkRequest request) {

        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Artwork not found")
                );

        Long userId = Long.valueOf(jwt.getSubject());

        if (!artwork.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to modify this artwork");
        }

        artwork.setTitle(request.getTitle());
        artwork.setDescription(request.getDescription());
        artwork.setImageUrl(request.getImageUrl());

        Artwork updatedArtwork = artworkRepository.save(artwork);

        return mapToResponse(updatedArtwork);
    }

    public void deleteArtwork(Long id, Jwt jwt) {

        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Artwork not found")
                );

        Long userId = Long.valueOf(jwt.getSubject());

        if (!artwork.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this artwork");
        }

        artworkRepository.delete(artwork);
    }

    public List<ArtworkResponse> searchArtworks(String query) {

        return artworkRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
                        query,
                        query
                )
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