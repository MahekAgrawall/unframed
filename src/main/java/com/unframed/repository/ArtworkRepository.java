package com.unframed.repository;

import com.unframed.entity.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    List<Artwork> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds);

    List<Artwork> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
            String title,
            String description
    );
}