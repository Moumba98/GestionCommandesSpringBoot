package com.example.gestion_clients.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void testTokenGenerationAndExtraction() {
        // 1. ARRANGE : On prépare les données
        String username = "amine";
        String role = "ADMIN";

        // 2. ACT : On exécute la méthode à tester
        String token = jwtUtils.generateToken(username, role);
        String extractedName = jwtUtils.getUsernameFromToken(token);

        // 3. ASSERT : On vérifie le résultat

        assertNotNull(token); // Le token ne doit pas être vide
        assertEquals(username, extractedName); // Le nom extrait doit être le même
    }

}
