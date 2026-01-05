package com.example.gestion_clients.controller;


import com.example.gestion_clients.dto.ClientDTO;
import com.example.gestion_clients.model.ClientEntity;
import com.example.gestion_clients.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")

public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/ajouter")
    public ClientEntity creerClient(@Valid @RequestBody ClientDTO clientDTO) {
        return clientService.ajouterClient(clientDTO);
    }

    // afficher tous les clients

    @GetMapping("/liste")
    public List<ClientEntity> afficherTousLesClients() {
        return clientService.listerClients();
    }

    // recuperer un client par son id

    @GetMapping("/{id}")
    public ClientEntity recupererUnClient(@PathVariable Long id) {
        return clientService.trouverParId(id);
    }

    //  Modifier un client
    @PutMapping("/{id}")
    public ClientEntity modifier(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        return clientService.modifierClient(id, clientDTO);
    }


    // supprimer un client

    @DeleteMapping("/{id}")
    public String supprimer(@PathVariable Long id) {
        clientService.supprimerClient(id);
        return "Le client avec l'ID " + id + " a été supprimé avec succès.";
    }



}
