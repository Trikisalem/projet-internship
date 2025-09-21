package com.projectnote.note_bloc.repository;

import com.projectnote.note_bloc.entity.PublicLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicLinkRepository extends JpaRepository<PublicLink, Long> {
    
    Optional<PublicLink> findByUrlTokenAndActiveTrue(String urlToken);
}
