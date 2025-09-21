package com.projectnote.note_bloc.service;

import com.projectnote.note_bloc.dto.NoteRequest;
import com.projectnote.note_bloc.entity.Note;
import com.projectnote.note_bloc.entity.User;
import com.projectnote.note_bloc.entity.NoteShare;
import com.projectnote.note_bloc.entity.PublicLink;
import com.projectnote.note_bloc.repository.NoteRepository;
import com.projectnote.note_bloc.repository.UserRepository;
import com.projectnote.note_bloc.repository.NoteShareRepository;
import com.projectnote.note_bloc.repository.PublicLinkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NoteShareRepository noteShareRepository;
    
    @Autowired
    private PublicLinkRepository publicLinkRepository;
    
    /**
     * Récupérer toutes les notes d'un utilisateur avec filtres et pagination
     * GET /api/v1/notes?query=&tag=&visibility=&page=&size=
     */
    @Transactional(readOnly = true)
    public Page<Note> getNotes(String email, String query, String tag, 
                              Note.Visibility visibility, int page, int size) {
        
        User user = getUserByEmail(email);
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.DESC, "updatedAt"));
        
        // Application des filtres
        if (query != null && !query.trim().isEmpty()) {
            if (tag != null && !tag.trim().isEmpty()) {
                if (visibility != null) {
                    return noteRepository.findByUserAndTitleContainingIgnoreCaseAndTagsContainingAndVisibility(
                        user, query, tag, visibility, pageable);
                } else {
                    return noteRepository.findByUserAndTitleContainingIgnoreCaseAndTagsContaining(
                        user, query, tag, pageable);
                }
            } else {
                if (visibility != null) {
                    return noteRepository.findByUserAndTitleContainingIgnoreCaseAndVisibility(
                        user, query, visibility, pageable);
                } else {
                    return noteRepository.findByUserAndTitleContainingIgnoreCase(
                        user, query, pageable);
                }
            }
        } else if (tag != null && !tag.trim().isEmpty()) {
            if (visibility != null) {
                return noteRepository.findByUserAndTagsContainingAndVisibility(
                    user, tag, visibility, pageable);
            } else {
                return noteRepository.findByUserAndTagsContaining(user, tag, pageable);
            }
        } else if (visibility != null) {
            return noteRepository.findByUserAndVisibility(user, visibility, pageable);
        } else {
            return noteRepository.findByUser(user, pageable);
        }
    }
    
    /**
     * Récupérer une note spécifique par ID - GET /api/v1/notes/{id}
     */
    @Transactional(readOnly = true)
    public Note getNoteById(Long id, String email) {
        User user = getUserByEmail(email);
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Note non trouvée avec l'ID: " + id));
            
        // Vérifier ownership ou accès partagé
        if (!note.getUser().equals(user) && !hasReadAccess(note, user)) {
            throw new RuntimeException("Accès interdit à cette note");
        }
        
        return note;
    }
    
    /**
     * Créer une nouvelle note - POST /api/v1/notes
     */
    public Note createNote(NoteRequest noteRequest, String email) {
        User user = getUserByEmail(email);
        
        Note note = new Note();
        note.setTitle(noteRequest.getTitle());
        note.setContentMd(noteRequest.getContentMd()); // CORRIGÉ: contentMd au lieu de content
        note.setTags(noteRequest.getTags() != null ? noteRequest.getTags() : Set.of());
        note.setVisibility(noteRequest.getVisibility());
        note.setUser(user);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        
        return noteRepository.save(note);
    }
    
    /**
     * Mettre à jour une note - PUT /api/v1/notes/{id}
     */
    public Note updateNote(Long id, NoteRequest noteRequest, String email) {
        Note existingNote = getNoteById(id, email);
        
        // Vérifier ownership
        if (!existingNote.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Vous n'avez pas le droit de modifier cette note");
        }
        
        existingNote.setTitle(noteRequest.getTitle());
        existingNote.setContentMd(noteRequest.getContentMd()); // CORRIGÉ: contentMd
        existingNote.setTags(noteRequest.getTags() != null ? noteRequest.getTags() : Set.of());
        existingNote.setVisibility(noteRequest.getVisibility());
        existingNote.setUpdatedAt(LocalDateTime.now());
        
        return noteRepository.save(existingNote);
    }
    
    /**
     * Supprimer une note - DELETE /api/v1/notes/{id}
     */
    public void deleteNote(Long id, String email) {
        Note note = getNoteById(id, email);
        
        // Vérifier ownership
        if (!note.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer cette note");
        }
        
        noteRepository.delete(note);
    }
    
    /**
     * Partager une note avec un utilisateur - POST /api/v1/notes/{id}/share/user
     */
    public NoteShare shareNoteWithUser(Long noteId, String recipientEmail, String ownerEmail) {
        Note note = getNoteById(noteId, ownerEmail);
        User recipient = getUserByEmail(recipientEmail);
        
        // Vérifier ownership
        if (!note.getUser().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("Vous n'avez pas le droit de partager cette note");
        }
        
        // Vérifier si déjà partagé
        if (noteShareRepository.findByNoteAndSharedWithUser(note, recipient).isPresent()) {
            throw new RuntimeException("Cette note est déjà partagée avec cet utilisateur");
        }
        
        NoteShare noteShare = new NoteShare(note, recipient, NoteShare.Permission.READ);
        return noteShareRepository.save(noteShare);
    }
    
    /**
     * Créer un lien public - POST /api/v1/notes/{id}/share/public
     */
    public PublicLink createPublicLink(Long noteId, String ownerEmail) {
        Note note = getNoteById(noteId, ownerEmail);
        
        // Vérifier ownership
        if (!note.getUser().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("Vous n'avez pas le droit de créer un lien public pour cette note");
        }
        
        PublicLink publicLink = new PublicLink(note);
        note.setVisibility(Note.Visibility.PUBLIC);
        noteRepository.save(note);
        
        return publicLinkRepository.save(publicLink);
    }
    
    /**
     * Récupérer une note publique - GET /api/v1/p/{urlToken}
     */
    @Transactional(readOnly = true)
    public Note getPublicNote(String urlToken) {
        PublicLink publicLink = publicLinkRepository.findByUrlTokenAndActiveTrue(urlToken)
            .orElseThrow(() -> new RuntimeException("Lien public non trouvé ou expiré"));
            
        if (publicLink.isExpired()) {
            throw new RuntimeException("Ce lien public a expiré");
        }
        
        return publicLink.getNote();
    }
    
    /**
     * Supprimer un partage - DELETE /api/v1/shares/{shareId}
     */
    public void deleteShare(Long shareId, String userEmail) {
        NoteShare share = noteShareRepository.findById(shareId)
            .orElseThrow(() -> new RuntimeException("Partage non trouvé"));
            
        // Seul le propriétaire de la note ou le destinataire peut supprimer le partage
        if (!share.getNote().getUser().getEmail().equals(userEmail) && 
            !share.getSharedWithUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer ce partage");
        }
        
        noteShareRepository.delete(share);
    }
    
    /**
     * Supprimer un lien public - DELETE /api/v1/public-links/{id}
     */
    public void deletePublicLink(Long linkId, String userEmail) {
        PublicLink publicLink = publicLinkRepository.findById(linkId)
            .orElseThrow(() -> new RuntimeException("Lien public non trouvé"));
            
        // Seul le propriétaire de la note peut supprimer le lien
        if (!publicLink.getNote().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer ce lien public");
        }
        
        publicLinkRepository.delete(publicLink);
    }
    
    // ==================== MÉTHODES UTILITAIRES ====================
    
    /**
     * Récupérer un utilisateur par email
     */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));
    }
    
    /**
     * Vérifier si un utilisateur a accès en lecture à une note
     */
    private boolean hasReadAccess(Note note, User user) {
        // Vérifier si la note est partagée avec l'utilisateur
        return noteShareRepository.findByNoteAndSharedWithUser(note, user).isPresent() ||
               note.getVisibility() == Note.Visibility.PUBLIC;
    }
    
    /**
     * Rechercher des notes par titre (méthode legacy maintenue pour compatibilité)
     */
    @Transactional(readOnly = true)
    public List<Note> searchNotesByTitle(String title, String email) {
        User user = getUserByEmail(email);
        return noteRepository.findByUserAndTitleContainingIgnoreCase(user, title);
    }
    
    /**
     * Compter le nombre total de notes d'un utilisateur
     */
    @Transactional(readOnly = true)
    public long countNotesByUser(String email) {
        User user = getUserByEmail(email);
        return noteRepository.countByUser(user);
    }
    
    /**
     * Récupérer les notes récentes d'un utilisateur
     */
    @Transactional(readOnly = true)
    public List<Note> getRecentNotes(String email, int limit) {
        Pageable pageable = PageRequest.of(0, limit, 
            Sort.by(Sort.Direction.DESC, "updatedAt"));
        User user = getUserByEmail(email);
        return noteRepository.findByUser(user, pageable).getContent();
    }
}
