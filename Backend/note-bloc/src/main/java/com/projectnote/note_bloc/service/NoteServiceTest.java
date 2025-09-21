package com.projectnote.note_bloc.service;

import com.projectnote.note_bloc.dto.NoteRequest;
import com.projectnote.note_bloc.entity.Note;
import com.projectnote.note_bloc.entity.User;
import com.projectnote.note_bloc.exception.UserNotFoundException;
import com.projectnote.note_bloc.repository.NoteRepository;
import com.projectnote.note_bloc.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("🧪 Tests unitaires NoteService")
class NoteServiceTest {
    
    @Mock
    private NoteRepository noteRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private NoteService noteService;
    
    private User testUser;
    private NoteRequest noteRequest;
    
    @BeforeEach
    void setUp() {
        // Préparation des données de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        
        noteRequest = new NoteRequest();
        noteRequest.setTitle("Test Note");
        noteRequest.setContentMd("# Test content\n\nCeci est un **test**");
        noteRequest.setTags(new HashSet<>(Set.of("test", "junit")));
        noteRequest.setVisibility(Note.Visibility.PRIVATE);
    }
    
    @Test
    @DisplayName("✅ Créer une note - Cas de succès")
    void createNote_ShouldReturnNote_WhenValidInput() {
        // Given
        String userEmail = "test@example.com";
        
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(testUser));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note note = invocation.getArgument(0);
            note.setId(1L);
            note.setCreatedAt(LocalDateTime.now());
            note.setUpdatedAt(LocalDateTime.now());
            return note;
        });
        
        // When
        Note result = noteService.createNote(noteRequest, userEmail);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Note");
        assertThat(result.getContentMd()).isEqualTo("# Test content\n\nCeci est un **test**");
        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getTags()).containsExactlyInAnyOrder("test", "junit");
        assertThat(result.getVisibility()).isEqualTo(Note.Visibility.PRIVATE);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        
        // Vérifier les interactions avec les mocks
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(noteRepository, times(1)).save(any(Note.class));
        verifyNoMoreInteractions(userRepository, noteRepository);
    }
    
    @Test
    @DisplayName("❌ Créer une note - Utilisateur non trouvé")
    void createNote_ShouldThrowException_WhenUserNotFound() {
        // Given
        String userEmail = "notfound@example.com";
        
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> noteService.createNote(noteRequest, userEmail))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("Utilisateur non trouvé avec l'email: " + userEmail);
        
        // Vérifier qu'aucune note n'a été sauvegardée
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(noteRepository, never()).save(any(Note.class));
    }
    
    @Test
    @DisplayName("✅ Créer une note - Sans tags")
    void createNote_ShouldCreateNote_WhenNoTags() {
        // Given
        String userEmail = "test@example.com";
        noteRequest.setTags(new HashSet<>()); // Aucun tag
        
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(testUser));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note note = invocation.getArgument(0);
            note.setId(2L);
            return note;
        });
        
        // When
        Note result = noteService.createNote(noteRequest, userEmail);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTags()).isEmpty();
        assertThat(result.getTitle()).isEqualTo("Test Note");
        
        verify(userRepository).findByEmail(userEmail);
        verify(noteRepository).save(any(Note.class));
    }
    
    @Test
    @DisplayName("✅ Créer une note - Visibilité PUBLIC")
    void createNote_ShouldCreateNote_WhenPublicVisibility() {
        // Given
        String userEmail = "test@example.com";
        noteRequest.setVisibility(Note.Visibility.PUBLIC);
        
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(testUser));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note note = invocation.getArgument(0);
            note.setId(3L);
            return note;
        });
        
        // When
        Note result = noteService.createNote(noteRequest, userEmail);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getVisibility()).isEqualTo(Note.Visibility.PUBLIC);
        
        verify(userRepository).findByEmail(userEmail);
        verify(noteRepository).save(any(Note.class));
    }
}
