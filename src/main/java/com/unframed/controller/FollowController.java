package com.unframed.controller;

import com.unframed.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{userId}/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleFollow(
            @PathVariable Long userId,
            @AuthenticationPrincipal Jwt jwt) {

        followService.toggleFollow(userId, jwt);
    }
}
