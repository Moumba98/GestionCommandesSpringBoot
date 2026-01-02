package com.example.gestion_clients.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommandeDTO {

    @NotBlank(message = "Le nom du produit est obligatoire")
    private String produit;

    @Min(value = 1, message = "La quantité doit être au moins de 1")
    @NotNull(message = "La quantité est obligatoire")
    private int quantite;

    @NotNull(message = "L'ID du client est obligatoire pour lier la commande")
    private Long clientId;
}
