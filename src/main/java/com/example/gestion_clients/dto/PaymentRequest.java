package com.example.gestion_clients.dto;



import lombok.Data;

@Data
public class PaymentRequest {
    private Double amount; // Le montant total du panier (ex: 99.99)
}
