package com.unframed.controller;

import com.unframed.dto.CommentRequest;
import com.unframed.dto.CommentResponse;
import com.unframed.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artworks")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{artworkId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(
            @PathVariable Long artworkId,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CommentRequest request) {

        return commentService.addComment(
                artworkId,
                jwt,
                request
        );
    }

    @GetMapping("/{artworkId}/comments")
    public List<CommentResponse> getComments(
            @PathVariable Long artworkId) {

        return commentService.getComments(artworkId);
    }
}