package com.example.gestion_clients.controller;

import com.example.gestion_clients.model.UserEntity;
import com.example.gestion_clients.repository.UserRepository;
import com.example.gestion_clients.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // 1. ENREGISTRER UN UTILISATEUR
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Nom d'utilisateur déjà utilisé !");
        }

        // IMPORTANT : Si le rôle n'est pas précisé dans le JSON, on met "USER" par défaut
        user.setRole("USER");


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Utilisateur créé avec succès avec le rôle User par defaut : " + user.getRole());
    }

    // 2. SE CONNECTER ET RÉCUPÉRER LE TOKEN (AVEC RÔLE)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity loginRequest) {

        // On cherche l'utilisateur
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // On compare les mots de passe
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            // --- MODIFICATION ICI : On passe aussi le RÔLE au token ---
            String token = jwtUtils.generateToken(user.getUsername(), user.getRole());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole()); // Optionnel : on le renvoie aussi en clair pour le frontend

            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(401).body("Mot de passe incorrect !");
        }
    }

    // chager le role de l'utilisteur

    @PutMapping("/change-role/{username}")
    public ResponseEntity<String> changeRole(@PathVariable String username, @RequestParam String newRole) {
        // On cherche l'utilisateur dont on veut changer le rôle
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // On met à jour le rôle (ex: passer de USER à ADMIN)
        user.setRole(newRole.toUpperCase());
        userRepository.save(user);

        return ResponseEntity.ok("Le rôle de " + username + " a été mis à jour en : " + newRole);
    }
}