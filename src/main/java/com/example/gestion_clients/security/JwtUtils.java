package com.example.gestion_clients.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Récupération des valeurs de ton application.properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationMs;

    // Cette méthode transforme ta chaîne de caractères en une clé sécurisée

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. MÉTHODE POUR GÉNÉRER LE TOKEN
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // On ajoute le rôle ici !
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //  MÉTHODE POUR VALIDER LE TOKEN
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Le token est soit expiré, soit mal formé, soit la signature est fausse
            System.out.println("Erreur JWT : " + e.getMessage());
        }
        return false;
    }

    //  MÉTHODE POUR EXTRAIRE LE NOM DE L'UTILISATEUR

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // MÉTHODE POUR EXTRAIRE LE RÔLE DE L'UTILISATEUR
    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class); // On récupère l'info "role" que tu as mise au moment de la génération
    }


}