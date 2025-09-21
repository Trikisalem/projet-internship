package com.projectnote.note_bloc.controller;

import com.projectnote.note_bloc.dto.NoteRequest;
import com.projectnote.note_bloc.dto.ShareUserRequest;
import com.projectnote.note_bloc.entity.Note;
import com.projectnote.note_bloc.entity.NoteShare;
import com.projectnote.note_bloc.entity.PublicLink;
import com.projectnote.note_bloc.service.NoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")  // CORRIGÉ: /api/v1 au lieu de /api/notes
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Notes", description = "API de gestion des notes")
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    /**
     * GET /api/v1/notes?query=&tag=&visibility=&page=&size=
     * Récupérer les notes avec pagination et filtres
     */
    @GetMapping
    @Operation(summary = "Récupérer les notes avec filtres et pagination")
    public ResponseEntity<Page<Note>> getNotes(
            @Parameter(description = "Recherche par titre") @RequestParam(required = false) String query,
            @Parameter(description = "Filtrer par tag") @RequestParam(required = false) String tag,
            @Parameter(description = "Filtrer par visibilité") @RequestParam(required = false) Note.Visibility visibility,
            @Parameter(description = "Numéro de page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de page") @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        
        Page<Note> notes = noteService.getNotes(auth.getName(), query, tag, visibility, page, size);
        return ResponseEntity.ok(notes);
    }
    
    /**
     * GET /api/v1/notes/{id}
     * Récupérer une note spécifique par ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une note par ID")
    @ApiResponse(responseCode = "200", description = "Note trouvée")
    @ApiResponse(responseCode = "404", description = "Note non trouvée")
    @ApiResponse(responseCode = "403", description = "Accès interdit")
    public ResponseEntity<Note> getNoteById(
            @Parameter(description = "ID de la note") @PathVariable Long id,
            Authentication auth) {
        
        Note note = noteService.getNoteById(id, auth.getName());
        return ResponseEntity.ok(note);
    }
    
    /**
     * POST /api/v1/notes
     * Créer une nouvelle note
     */
    @PostMapping
    @Operation(summary = "Créer une nouvelle note")
    @ApiResponse(responseCode = "200", description = "Note créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public ResponseEntity<Note> createNote(
            @Valid @RequestBody NoteRequest noteRequest,  // CORRIGÉ: NoteRequest au lieu de Note
            Authentication auth) {
        
        Note createdNote = noteService.createNote(noteRequest, auth.getName());
        return ResponseEntity.ok(createdNote);
    }
    
    /**
     * PUT /api/v1/notes/{id}
     * Mettre à jour une note
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une note")
    @ApiResponse(responseCode = "200", description = "Note mise à jour")
    @ApiResponse(responseCode = "404", description = "Note non trouvée")
    @ApiResponse(responseCode = "403", description = "Accès interdit")
    public ResponseEntity<Note> updateNote(
            @Parameter(description = "ID de la note") @PathVariable Long id,
            @Valid @RequestBody NoteRequest noteRequest,  // CORRIGÉ: NoteRequest au lieu de Note
            Authentication auth) {
        
        Note updatedNote = noteService.updateNote(id, noteRequest, auth.getName());
        return ResponseEntity.ok(updatedNote);
    }
    
    /**
     * DELETE /api/v1/notes/{id}
     * Supprimer une note
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une note")
    @ApiResponse(responseCode = "200", description = "Note supprimée")
    @ApiResponse(responseCode = "404", description = "Note non trouvée")
    @ApiResponse(responseCode = "403", description = "Accès interdit")
    public ResponseEntity<Void> deleteNote(
            @Parameter(description = "ID de la note") @PathVariable Long id,
            Authentication auth) {
        
        noteService.deleteNote(id, auth.getName());
        return ResponseEntity.ok().build();
    }
    
    /**
     * POST /api/v1/notes/{id}/share/user
     * Partager une note avec un utilisateur
     */
    @PostMapping("/{id}/share/user")
    @Operation(summary = "Partager une note avec un utilisateur")
    @ApiResponse(responseCode = "200", description = "Note partagée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Note ou utilisateur non trouvé")
    public ResponseEntity<NoteShare> shareNoteWithUser(
            @Parameter(description = "ID de la note") @PathVariable Long id,
            @Valid @RequestBody ShareUserRequest shareRequest,
            Authentication auth) {
        
        NoteShare noteShare = noteService.shareNoteWithUser(id, shareRequest.getEmail(), auth.getName());
        return ResponseEntity.ok(noteShare);
    }
    
    /**
     * POST /api/v1/notes/{id}/share/public
     * Créer un lien public pour une note
     */
    @PostMapping("/{id}/share/public")
    @Operation(summary = "Créer un lien public pour une note")
    @ApiResponse(responseCode = "200", description = "Lien public créé")
    @ApiResponse(responseCode = "404", description = "Note non trouvée")
    @ApiResponse(responseCode = "403", description = "Accès interdit")
    public ResponseEntity<PublicLink> createPublicLink(
            @Parameter(description = "ID de la note") @PathVariable Long id,
            Authentication auth) {
        
        PublicLink publicLink = noteService.createPublicLink(id, auth.getName());
        return ResponseEntity.ok(publicLink);
    }
    
    /**
     * DELETE /api/v1/shares/{shareId}
     * Supprimer un partage
     */
    @DeleteMapping("/shares/{shareId}")
    @Operation(summary = "Supprimer un partage")
    @ApiResponse(responseCode = "200", description = "Partage supprimé")
    @ApiResponse(responseCode = "404", description = "Partage non trouvé")
    public ResponseEntity<Void> deleteShare(
            @Parameter(description = "ID du partage") @PathVariable Long shareId,
            Authentication auth) {
        
        noteService.deleteShare(shareId, auth.getName());
        return ResponseEntity.ok().build();
    }
    
    /**
     * DELETE /api/v1/public-links/{id}
     * Supprimer un lien public
     */
    @DeleteMapping("/public-links/{id}")
    @Operation(summary = "Supprimer un lien public")
    @ApiResponse(responseCode = "200", description = "Lien public supprimé")
    @ApiResponse(responseCode = "404", description = "Lien public non trouvé")
    public ResponseEntity<Void> deletePublicLink(
            @Parameter(description = "ID du lien public") @PathVariable Long id,
            Authentication auth) {
        
        noteService.deletePublicLink(id, auth.getName());
        return ResponseEntity.ok().build();
    }
}
