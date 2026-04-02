
package com.example.gestion_clients.security;
import com.example.gestion_clients.security.JwtFilter;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


import java.util.Arrays;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://15.188.37.53:4200","http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Utiliser "*" pour les headers permet d'éviter les erreurs bêtes
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Activer le CORS AVANT TOUT LE RESTE
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 2. Autoriser les requêtes de pré-vérification (OPTIONS) pour le navigateur
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // Autoriser tout le monde à voir les produits
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(antMatcher("/api/payment/**")).permitAll()

                        // 3. Accès libre pour l'authentification
                        //.requestMatchers("/api/auth/**").permitAll()

                        // On laisse l'accès libre UNIQUEMENT pour login et register
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // On protège le changement de rôle et la liste : SEUL l'ADMIN peut y toucher
                        .requestMatchers("/api/auth/change-role/**", "/api/auth/users").hasRole("ADMIN")
                        .requestMatchers("/api/auth/users").hasRole("ADMIN")
                        // Remplace .hasRole par .hasAuthority
                        .requestMatchers("/api/auth/change-role/**", "/api/auth/users").hasRole("ADMIN")
                        // Sécurisation du paiement : il faut être authentifié



                        // Seul l'Admin peut modifier le catalogue
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")

                        // Seul l'Admin peut supprimer des gens (Utilisateur)
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/auth/delete/**").hasRole("ADMIN")

                        // 4. Tes restrictions par rôles
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/clients/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/clients/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/clients/**").hasRole("ADMIN")

                        // Commandes
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/commandes/**").hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/commandes/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/commandes/**").hasAnyRole("ADMIN", "USER")

                        // Consultation
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/clients/**", "/api/commandes/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
