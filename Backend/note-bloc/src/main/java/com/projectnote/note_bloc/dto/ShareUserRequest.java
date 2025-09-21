package com.projectnote.note_bloc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO pour partager une note avec un utilisateur
 * Utilisé pour l'endpoint POST /api/v1/notes/{id}/share/user
 */
@Schema(description = "Demande de partage de note avec un utilisateur")
public class ShareUserRequest {
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide", 
           regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    @Schema(description = "Email de l'utilisateur avec qui partager la note", 
            example = "utilisateur@example.com")
    private String email;
    
    @Schema(description = "Permission accordée (READ ou WRITE)", 
            example = "READ", 
            defaultValue = "READ")
    private String permission = "READ";
    
    @Schema(description = "Message optionnel à envoyer avec le partage", 
            example = "Je partage cette note avec vous")
    private String message;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructeur par défaut
     */
    public ShareUserRequest() {}
    
    /**
     * Constructeur avec email seulement
     */
    public ShareUserRequest(String email) {
        this.email = email;
    }
    
    /**
     * Constructeur complet
     */
    public ShareUserRequest(String email, String permission, String message) {
        this.email = email;
        this.permission = permission != null ? permission : "READ";
        this.message = message;
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    /**
     * Récupérer l'email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Définir l'email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Récupérer la permission
     */
    public String getPermission() {
        return permission;
    }
    
    /**
     * Définir la permission
     */
    public void setPermission(String permission) {
        this.permission = permission != null ? permission : "READ";
    }
    
    /**
     * Récupérer le message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Définir le message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Vérifier si l'email est valide (méthode utilitaire)
     */
    public boolean isValidEmail() {
        return email != null && 
               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Convertir la permission en enum
     */
    public com.projectnote.note_bloc.entity.NoteShare.Permission getPermissionEnum() {
        try {
            return com.projectnote.note_bloc.entity.NoteShare.Permission.valueOf(
                permission.toUpperCase()
            );
        } catch (Exception e) {
            return com.projectnote.note_bloc.entity.NoteShare.Permission.READ;
        }
    }
    
    // ==================== OVERRIDE METHODS ====================
    
    @Override
    public String toString() {
        return "ShareUserRequest{" +
                "email='" + email + '\'' +
                ", permission='" + permission + '\'' +
                ", message='" + (message != null ? message.substring(0, Math.min(50, message.length())) + "..." : "null") + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ShareUserRequest that = (ShareUserRequest) obj;
        return email != null ? email.equals(that.email) : that.email == null;
    }
    
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
