package com.unframed.service;

import com.unframed.dto.CommentRequest;
import com.unframed.dto.CommentResponse;
import com.unframed.entity.Artwork;
import com.unframed.entity.Comment;
import com.unframed.entity.User;
import com.unframed.exception.UserNotFoundException;
import com.unframed.repository.ArtworkRepository;
import com.unframed.repository.CommentRepository;
import com.unframed.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    public CommentService(
            CommentRepository commentRepository,
            UserRepository userRepository,
            ArtworkRepository artworkRepository) {

        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    public CommentResponse addComment(
            Long artworkId,
            Jwt jwt,
            CommentRequest request) {

        Long userId = Long.valueOf(jwt.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() ->
                        new RuntimeException("Artwork not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setArtwork(artwork);

        return mapToResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> getComments(Long artworkId) {

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() ->
                        new RuntimeException("Artwork not found"));

        return commentRepository
                .findByArtworkOrderByCreatedAtDesc(artwork)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CommentResponse mapToResponse(Comment comment) {

        CommentResponse response = new CommentResponse();

        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUserId(comment.getUser().getId());
        response.setUserName(comment.getUser().getName());
        response.setCreatedAt(comment.getCreatedAt());

        return response;
    }
}
