package com.projectnote.note_bloc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "note_shares")
public class NoteShare {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_user_id")
    private User sharedWithUser;
    
    @Enumerated(EnumType.STRING)
    private Permission permission = Permission.READ;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public NoteShare() {
        this.createdAt = LocalDateTime.now();
    }
    
    public NoteShare(Note note, User sharedWithUser, Permission permission) {
        this();
        this.note = note;
        this.sharedWithUser = sharedWithUser;
        this.permission = permission;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Note getNote() { return note; }
    public void setNote(Note note) { this.note = note; }
    
    public User getSharedWithUser() { return sharedWithUser; }
    public void setSharedWithUser(User sharedWithUser) { this.sharedWithUser = sharedWithUser; }
    
    public Permission getPermission() { return permission; }
    public void setPermission(Permission permission) { this.permission = permission; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public enum Permission {
        READ, WRITE
    }
}
