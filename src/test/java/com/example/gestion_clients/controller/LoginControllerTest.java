package com.example.gestion_clients.controller;

import com.example.gestion_clients.model.UserEntity;
import com.example.gestion_clients.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock; // Import nécessaire
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private UserRepository userRepository; // Mockito va créer ce faux repo

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginController loginController; // Mockito injectera le faux repo et le faux encoder ici

    @Test
    void registerShouldAlwaysForceUserRole() {
        // 1. ARRANGE
        UserEntity userTentative = new UserEntity();
        userTentative.setUsername("pirate");
        userTentative.setPassword("1234");
        userTentative.setRole("ADMIN");

        // 2. ACT
        loginController.register(userTentative);

        // 3. ASSERT
        assertEquals("USER", userTentative.getRole(), "Le rôle aurait dû être forcé à USER");
    }
}