package com.projectnote.note_bloc.repository;

import com.projectnote.note_bloc.entity.Note;
import com.projectnote.note_bloc.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    // ==================== MÉTHODES DE BASE CORRIGÉES ====================
    
    /**
     * Recherche par email utilisateur - CORRIGÉ
     */
    @Query("SELECT n FROM Note n WHERE n.user.email = :email ORDER BY n.updatedAt DESC")
    List<Note> findByUserEmail(@Param("email") String email);
    
    /**
     * Recherche par ID et email utilisateur - CORRIGÉ
     */
    @Query("SELECT n FROM Note n WHERE n.id = :id AND n.user.email = :email")
    Optional<Note> findByIdAndUserEmail(@Param("id") Long id, @Param("email") String email);
    
    /**
     * Recherche par utilisateur (entité User)
     */
    List<Note> findByUser(User user);
    
    /**
     * Recherche par utilisateur avec pagination
     */
    Page<Note> findByUser(User user, Pageable pageable);
    
    /**
     * Compter les notes d'un utilisateur
     */
    long countByUser(User user);
    
    // ==================== RECHERCHE PAR TITRE ====================
    
    /**
     * Recherche par titre (insensible à la casse)
     */
    List<Note> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Recherche par utilisateur et titre - CORRIGÉ pour compatibilité
     */
    List<Note> findByUserAndTitleContainingIgnoreCase(User user, String title);
    
    /**
     * Recherche par utilisateur et titre avec pagination
     */
    Page<Note> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);
    
    // ==================== FILTRES PAR VISIBILITÉ ====================
    
    /**
     * Recherche par utilisateur et visibilité
     */
    Page<Note> findByUserAndVisibility(User user, Note.Visibility visibility, Pageable pageable);
    
    /**
     * Recherche par utilisateur, titre et visibilité
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
           "n.visibility = :visibility")
    Page<Note> findByUserAndTitleContainingIgnoreCaseAndVisibility(
        @Param("user") User user, @Param("query") String query, 
        @Param("visibility") Note.Visibility visibility, Pageable pageable);
    
    // ==================== FILTRES PAR TAGS ====================
    
    /**
     * Recherche par utilisateur et tags
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND :tag MEMBER OF n.tags")
    Page<Note> findByUserAndTagsContaining(@Param("user") User user, @Param("tag") String tag, Pageable pageable);
    
    /**
     * Recherche par utilisateur, tags et visibilité
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND :tag MEMBER OF n.tags AND n.visibility = :visibility")
    Page<Note> findByUserAndTagsContainingAndVisibility(
        @Param("user") User user, @Param("tag") String tag, 
        @Param("visibility") Note.Visibility visibility, Pageable pageable);
    
    // ==================== FILTRES COMBINÉS ====================
    
    /**
     * Recherche par utilisateur, titre et tags
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
           ":tag MEMBER OF n.tags")
    Page<Note> findByUserAndTitleContainingIgnoreCaseAndTagsContaining(
        @Param("user") User user, @Param("query") String query, 
        @Param("tag") String tag, Pageable pageable);
    
    /**
     * Recherche par utilisateur, titre, tags et visibilité
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
           ":tag MEMBER OF n.tags AND n.visibility = :visibility")
    Page<Note> findByUserAndTitleContainingIgnoreCaseAndTagsContainingAndVisibility(
        @Param("user") User user, @Param("query") String query, @Param("tag") String tag,
        @Param("visibility") Note.Visibility visibility, Pageable pageable);
    
    // ==================== MÉTHODES UTILITAIRES ====================
    
    /**
     * Recherche par visibilité
     */
    List<Note> findByVisibility(Note.Visibility visibility);
    
    /**
     * Recherche par visibilité avec pagination
     */
    Page<Note> findByVisibility(Note.Visibility visibility, Pageable pageable);
    
    /**
     * Supprimer toutes les notes d'un utilisateur
     */
    void deleteByUser(User user);
    
    /**
     * Compter les notes par visibilité pour un utilisateur
     */
    long countByUserAndVisibility(User user, Note.Visibility visibility);
    
    /**
     * Vérifier si une note appartient à un utilisateur
     */
    boolean existsByIdAndUser(Long id, User user);
}
