package com.projectnote.note_bloc.service;

import com.projectnote.note_bloc.dto.LoginRequest;
import com.projectnote.note_bloc.dto.SignupRequest;
import com.projectnote.note_bloc.entity.User;
import com.projectnote.note_bloc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Authentification réussie pour: " + user.getEmail();
        } else {
            return "Email ou mot de passe incorrect";
        }
    }
    
    public String registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email déjà utilisé";
        }
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword);
        userRepository.save(user);
        
        return "Utilisateur créé avec succès";
    }
}
