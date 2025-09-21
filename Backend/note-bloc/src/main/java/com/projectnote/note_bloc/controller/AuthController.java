package com.projectnote.note_bloc.controller;

import com.projectnote.note_bloc.dto.LoginRequest;
import com.projectnote.note_bloc.dto.SignupRequest;
import com.projectnote.note_bloc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignupRequest request) {
        String result = authService.registerUser(request);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String result = authService.authenticate(request);
        return ResponseEntity.ok(result);
    }
}
