package com.unframed.controller;

import com.unframed.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artworks")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{artworkId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleLike(
            @PathVariable Long artworkId,
            @AuthenticationPrincipal Jwt jwt) {

        likeService.toggleLike(artworkId, jwt);
    }

    @GetMapping("/{artworkId}/likes")
    public long getLikeCount(
            @PathVariable Long artworkId) {

        return likeService.getLikeCount(artworkId);
    }
}
