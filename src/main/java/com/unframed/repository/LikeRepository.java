package com.unframed.repository;

import com.unframed.entity.Artwork;
import com.unframed.entity.Like;
import com.unframed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndArtwork(User user, Artwork artwork);

    long countByArtwork(Artwork artwork);
}