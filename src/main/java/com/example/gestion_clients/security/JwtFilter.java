package com.example.gestion_clients.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. On récupère le header "Authorization" de la requête
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2. On vérifie si le header contient un Bearer Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // On enlève "Bearer " pour garder juste le code
            username = jwtUtils.getUsernameFromToken(token); // On extrait le nom de l'utilisateur
        }

        // 3. Si on a un utilisateur et qu'il n'est pas encore authentifié dans le contexte

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // On demande à JwtUtils de vérifier si le badge est toujours valide
            if (jwtUtils.validateToken(token)) {

                // --- NOUVEAUTÉ : ON RÉCUPÈRE LE RÔLE ---
                String role = jwtUtils.getRoleFromToken(token);

                // On crée une "Autorité" que Spring comprend.
                // IMPORTANT : On ajoute "ROLE_" car .hasRole("ADMIN") cherche "ROLE_ADMIN"
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                // On crée l'objet d'authentification en lui donnant la liste des autorités (rôles)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        java.util.Collections.singletonList(authority) // On passe le rôle ici !
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // On enregistre l'utilisateur AVEC son rôle dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // 4. On laisse la requête continuer son chemin vers le Controller
        filterChain.doFilter(request, response);
    }
}