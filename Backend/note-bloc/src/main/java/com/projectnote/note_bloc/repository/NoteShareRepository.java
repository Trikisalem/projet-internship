package com.projectnote.note_bloc.repository;

import com.projectnote.note_bloc.entity.Note;
import com.projectnote.note_bloc.entity.NoteShare;
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
public interface NoteShareRepository extends JpaRepository<NoteShare, Long> {
    
    /**
     * Trouver un partage spécifique entre une note et un utilisateur
     */
    Optional<NoteShare> findByNoteAndSharedWithUser(Note note, User sharedWithUser);
    
    /**
     * Trouver tous les partages d'une note spécifique
     */
    List<NoteShare> findByNote(Note note);
    
    /**
     * Trouver toutes les notes partagées AVEC un utilisateur (notes qu'il peut voir)
     */
    List<NoteShare> findBySharedWithUser(User sharedWithUser);
    
    /**
     * Trouver toutes les notes partagées AVEC un utilisateur avec pagination
     */
    Page<NoteShare> findBySharedWithUser(User sharedWithUser, Pageable pageable);
    
    /**
     * Trouver tous les partages créés par un propriétaire de notes
     */
    @Query("SELECT ns FROM NoteShare ns WHERE ns.note.user = :user")
    List<NoteShare> findByNoteOwner(@Param("user") User user);
    
    /**
     * Trouver tous les partages créés par un propriétaire avec pagination
     */
    @Query("SELECT ns FROM NoteShare ns WHERE ns.note.user = :user")
    Page<NoteShare> findByNoteOwner(@Param("user") User user, Pageable pageable);
    
    /**
     * Vérifier si une note est partagée avec un utilisateur spécifique
     */
    boolean existsByNoteAndSharedWithUser(Note note, User sharedWithUser);
    
    /**
     * Compter le nombre de partages d'une note
     */
    long countByNote(Note note);
    
    /**
     * Compter le nombre de notes partagées avec un utilisateur
     */
    long countBySharedWithUser(User sharedWithUser);
    
    /**
     * Trouver les partages par permission spécifique
     */
    List<NoteShare> findByPermission(NoteShare.Permission permission);
    
    /**
     * Trouver les partages d'une note avec permission spécifique
     */
    List<NoteShare> findByNoteAndPermission(Note note, NoteShare.Permission permission);
    
    /**
     * Trouver les notes partagées avec un utilisateur avec permission READ
     */
    List<NoteShare> findBySharedWithUserAndPermission(User sharedWithUser, NoteShare.Permission permission);
    
    /**
     * Supprimer tous les partages d'une note (utilisé lors de la suppression d'une note)
     */
    void deleteByNote(Note note);
    
    /**
     * Supprimer tous les partages avec un utilisateur spécifique
     */
    void deleteBySharedWithUser(User sharedWithUser);
    
    /**
     * Requête personnalisée pour trouver les notes accessibles à un utilisateur
     * (soit ses propres notes, soit les notes partagées avec lui)
     */
    @Query("SELECT DISTINCT n FROM Note n LEFT JOIN NoteShare ns ON n.id = ns.note.id " +
           "WHERE n.user = :user OR ns.sharedWithUser = :user")
    List<Note> findAccessibleNotes(@Param("user") User user);
    
    /**
     * Requête personnalisée pour trouver les notes accessibles avec pagination
     */
    @Query("SELECT DISTINCT n FROM Note n LEFT JOIN NoteShare ns ON n.id = ns.note.id " +
           "WHERE n.user = :user OR ns.sharedWithUser = :user")
    Page<Note> findAccessibleNotes(@Param("user") User user, Pageable pageable);
    
    /**
     * Trouver les partages par email de l'utilisateur partageur
     */
    @Query("SELECT ns FROM NoteShare ns WHERE ns.sharedWithUser.email = :email")
    List<NoteShare> findBySharedWithUserEmail(@Param("email") String email);
    
    /**
     * Trouver les partages où l'utilisateur est le propriétaire de la note
     */
    @Query("SELECT ns FROM NoteShare ns WHERE ns.note.user.email = :email")
    List<NoteShare> findByNoteOwnerEmail(@Param("email") String email);
    
    /**
     * Vérifier si un utilisateur peut accéder à une note (propriétaire ou partagé)
     */
    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END " +
           "FROM Note n LEFT JOIN NoteShare ns ON n.id = ns.note.id " +
           "WHERE n.id = :noteId AND (n.user.email = :userEmail OR ns.sharedWithUser.email = :userEmail)")
    boolean canUserAccessNote(@Param("noteId") Long noteId, @Param("userEmail") String userEmail);
    
    /**
     * Trouver les notes partagées avec des permissions d'écriture pour un utilisateur
     */
    @Query("SELECT ns.note FROM NoteShare ns " +
           "WHERE ns.sharedWithUser.email = :userEmail AND ns.permission = 'WRITE'")
    List<Note> findWritableNotesForUser(@Param("userEmail") String userEmail);
    
    /**
     * Compter les notes partagées par un utilisateur (en tant que propriétaire)
     */
    @Query("SELECT COUNT(DISTINCT ns.note) FROM NoteShare ns WHERE ns.note.user.email = :userEmail")
    long countSharedNotesByOwner(@Param("userEmail") String userEmail);
    
    /**
     * Compter les notes reçues en partage par un utilisateur
     */
    @Query("SELECT COUNT(ns) FROM NoteShare ns WHERE ns.sharedWithUser.email = :userEmail")
    long countReceivedSharesByUser(@Param("userEmail") String userEmail);
}
