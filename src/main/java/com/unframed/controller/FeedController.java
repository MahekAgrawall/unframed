package com.unframed.controller;

import com.unframed.dto.ArtworkResponse;
import com.unframed.service.FeedService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public List<ArtworkResponse> getFeed(
            @AuthenticationPrincipal Jwt jwt) {

        return feedService.getFeed(jwt);
    }
}