package com.example.gestion_clients.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "commande")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LIEN RÉEL avec la table Produit
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Product product;


    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false, length = 50)
    private String reference; // Pour grouper les produits d'un même panier

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(nullable = false)
    private LocalDateTime dateCommande;

    // Relation : Plusieurs commandes pour un seul client



    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("commandes")
    private UserEntity user; // On remplace ClientEntity par UserEntity
}
