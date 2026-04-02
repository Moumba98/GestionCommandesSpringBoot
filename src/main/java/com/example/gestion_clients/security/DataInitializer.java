package com.example.gestion_clients.security;

import com.example.gestion_clients.model.UserEntity;
import com.example.gestion_clients.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Injection par constructeur (recommandé)
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Définition des identifiants par défaut
        String username = "TESTVDE";

        // 2. Vérification si l'admin existe déjà (pour éviter les doublons au redémarrage Docker)
        if (userRepository.findByUsername(username).isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername(username);

            // On encode le mot de passe "admin123" avec BCrypt
            admin.setPassword(passwordEncoder.encode("TESTVDE"));

            // On met "ADMIN". Ton JwtFilter fera : "ROLE_" + "ADMIN" = "ROLE_ADMIN"
            admin.setRole("ADMIN");

            userRepository.save(admin);

            System.out.println("--------------------------------------------------");
            System.out.println("✅ ADMIN CRÉÉ : " + username);
            System.out.println("🔑 PASSWORD     : TESTVDE");
            System.out.println("--------------------------------------------------");
        } else {
            System.out.println("ℹ️ L'utilisateur TESTVDE existe déjà en base de données.");
        }
    }
}