package com.unframed.repository;

import com.unframed.entity.Artwork;
import com.unframed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArtworkOrderByCreatedAtDesc(Artwork artwork);
}