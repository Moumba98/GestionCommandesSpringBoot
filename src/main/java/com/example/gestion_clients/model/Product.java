package com.example.gestion_clients.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price; // Le prix unitaire

    private String imageUrl; // On stockera l'URL de l'image (ex: http://...)

    private int stockQuantity; // Pour savoir s'il en reste

    private String category; // Pour filtrer plus tard (ex: "Électronique", "Vêtements")
}
