package com.example.gestion_clients.controller;

import com.example.gestion_clients.dto.CommandeDTO;
import com.example.gestion_clients.model.CommandeEntity;
import com.example.gestion_clients.service.CommandeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @PostMapping("/ajouter")
    public CommandeEntity creerCommande(@Valid @RequestBody CommandeDTO commandeDTO) {
        return commandeService.ajouterCommande(commandeDTO);
    }

    @GetMapping("/liste")
    public List<CommandeEntity> lister() {
        return commandeService.listerToutesLesCommandes();
    }

    // recuperer une commande par son id

    @GetMapping("/{id}")
    public CommandeEntity recupererUneCommande(@PathVariable Long id) {
        return commandeService.trouverParId(id);
    }


    // MODIFIER une commande

    @PutMapping("/{id}")
    public CommandeEntity modifier(@PathVariable Long id, @Valid @RequestBody CommandeDTO dto) {
        return commandeService.modifierCommande(id, dto);
    }



    // SUPPRIMER une commande

    @DeleteMapping("/{id}")
    public String supprimer(@PathVariable Long id) {
        commandeService.supprimerCommande(id);
        return "La commande n" + id + " a été supprimée.";
    }

    @GetMapping("/mes-commandes")
    public List<CommandeEntity> obtenirMonHistorique(Authentication authentication) {
        // Le 'authentication.getName()' récupère l'email de l'utilisateur connecté via le Token JWT
        String email = authentication.getName();

        // On appelle la méthode qu'on a créée dans le service
        return commandeService.getHistoriqueParClient(email);
    }


}
