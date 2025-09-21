package com.projectnote.note_bloc.dto;

import com.projectnote.note_bloc.entity.Note;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class NoteRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 200, message = "Le titre doit contenir entre 3 et 200 caractères")
    private String title;
    
    @Size(max = 50000, message = "Le contenu ne peut pas dépasser 50000 caractères")
    private String contentMd;
    
    private Set<String> tags;
    
    private Note.Visibility visibility = Note.Visibility.PRIVATE;
    
    // Constructors
    public NoteRequest() {}
    
    public NoteRequest(String title, String contentMd, Set<String> tags) {
        this.title = title;
        this.contentMd = contentMd;
        this.tags = tags;
    }
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContentMd() { return contentMd; }
    public void setContentMd(String contentMd) { this.contentMd = contentMd; }
    
    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
    
    public Note.Visibility getVisibility() { return visibility; }
    public void setVisibility(Note.Visibility visibility) { this.visibility = visibility; }
}
