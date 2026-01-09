package com.example.gestion_clients.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Accès libre pour l'authentification
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. Restrictions par rôles (EXEMPLES)
                        // Seul l'ADMIN peut supprimer un client
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/clients/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/commandes/**").hasRole("ADMIN")
                        // SEUL ton utilisateur ADMIN pourra appeler cette URL
                        .requestMatchers("/api/auth/change-role/**").hasRole("ADMIN")

                        // Seul l'ADMIN peut ajouter ou modifier
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/clients/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/clients/**").hasRole("ADMIN")
                        //commandes
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/commandes/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/commandes/**").hasRole("ADMIN")

                        // L'USER et l'ADMIN peuvent consulter la liste
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/clients/**").hasAnyRole("USER", "ADMIN")

                        // 3. Sécurité par défaut pour le reste
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
