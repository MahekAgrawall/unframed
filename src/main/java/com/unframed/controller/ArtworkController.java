package com.unframed.controller;

import com.unframed.dto.ArtworkRequest;
import com.unframed.dto.ArtworkResponse;
import com.unframed.service.ArtworkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtworkResponse createArtwork(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ArtworkRequest request) {

        return artworkService.createArtwork(jwt, request);
    }

    @GetMapping
    public List<ArtworkResponse> getAllArtworks() {
        return artworkService.getAllArtworks();
    }

    @GetMapping("/search")
    public List<ArtworkResponse> searchArtworks(
            @RequestParam String query) {

        return artworkService.searchArtworks(query);
    }

    @GetMapping("/{id}")
    public ArtworkResponse getArtworkById(
            @PathVariable Long id) {

        return artworkService.getArtworkById(id);
    }

    @PutMapping("/{id}")
    public ArtworkResponse updateArtwork(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ArtworkRequest request) {

        return artworkService.updateArtwork(id, jwt, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtwork(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        artworkService.deleteArtwork(id, jwt);
    }
}