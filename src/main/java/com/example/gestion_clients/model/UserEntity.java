package com.example.gestion_clients.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // C'est ici que l'email servira d'identifiant (Username)
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // "ROLE_USER", "ROLE_ADMIN"

    // ON AJOUTE LE LIEN VERS LES COMMANDES ICI
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore // Pour éviter les boucles infinies en JSON
    private List<CommandeEntity> commandes;
}

