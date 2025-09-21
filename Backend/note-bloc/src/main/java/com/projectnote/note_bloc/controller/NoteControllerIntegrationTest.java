package com.projectnote.note_bloc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectnote.note_bloc.dto.LoginRequest;
import com.projectnote.note_bloc.dto.NoteRequest;
import com.projectnote.note_bloc.dto.SignupRequest;
import com.projectnote.note_bloc.entity.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NoteControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void happyPath_CreateAndManageNote() throws Exception {
        String email = "test@example.com";
        String password = "password123";
        
        // 1. Inscription
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
        
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Utilisateur créé avec succès"));
        
        // 2. Connexion
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();
        
        String token = extractTokenFromResponse(loginResult);
        
        // 3. Créer une note
        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setTitle("Note de test d'intégration");
        noteRequest.setContentMd("# Hello World\n\nCeci est un **test d'intégration** !");
        noteRequest.setTags(Set.of("test", "integration", "junit"));
        noteRequest.setVisibility(Note.Visibility.PRIVATE);
        
        MvcResult createResult = mockMvc.perform(post("/api/v1/notes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Note de test d'intégration"))
                .andExpect(jsonPath("$.contentMd").value("# Hello World\n\nCeci est un **test d'intégration** !"))
                .andExpect(jsonPath("$.tags").isArray())
                .andReturn();
        
        Long noteId = extractNoteIdFromResponse(createResult);
        
        // 4. Récupérer la note créée
        mockMvc.perform(get("/api/v1/notes/" + noteId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteId))
                .andExpect(jsonPath("$.title").value("Note de test d'intégration"));
        
        // 5. Lister les notes avec pagination
        mockMvc.perform(get("/api/v1/notes")
                .param("page", "0")
                .param("size", "10")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpected(jsonPath("$.content[0].title").value("Note de test d'intégration"))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        // 6. Rechercher des notes
        mockMvc.perform(get("/api/v1/notes")
                .param("query", "intégration")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpected(jsonPath("$.content[0].title").value("Note de test d'intégration"));
        
        // 7. Modifier la note
        noteRequest.setTitle("Note modifiée");
        mockMvc.perform(put("/api/v1/notes/" + noteId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Note modifiée"));
        
        // 8. Supprimer la note
        mockMvc.perform(delete("/api/v1/notes/" + noteId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        
        // 9. Vérifier que la note est supprimée
        mockMvc.perform(get("/api/v1/notes/" + noteId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
    
    private String extractTokenFromResponse(MvcResult result) throws Exception {
        String responseContent = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseContent).get("token").asText();
    }
    
    private Long extractNoteIdFromResponse(MvcResult result) throws Exception {
        String responseContent = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseContent).get("id").asLong();
    }
}
