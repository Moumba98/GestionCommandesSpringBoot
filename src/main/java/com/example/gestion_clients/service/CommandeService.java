package com.example.gestion_clients.service;


import com.example.gestion_clients.dto.CommandeDTO;
import com.example.gestion_clients.model.ClientEntity;
import com.example.gestion_clients.model.CommandeEntity;
import com.example.gestion_clients.repository.ClientRepository;
import com.example.gestion_clients.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ClientRepository clientRepository;


    // ajouter une commande ///////////////////////////////////


    public CommandeEntity ajouterCommande(CommandeDTO dto) {
        // 1. On cherche le client dans la base
        ClientEntity client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + dto.getClientId()));


        // 2. On crée la commande et on l'associe au client
        CommandeEntity commande = CommandeEntity.builder()
                .produit(dto.getProduit())
                .quantite(dto.getQuantite())
                .client(client) // <-- C'est ici que l'association se fait !
                .build();

        // 3. On sauvegarde
        return commandeRepository.save(commande);
    }



     // afficher toutes les commmendes /////////////////////////////////

    public List<CommandeEntity> listerToutesLesCommandes() {
        return commandeRepository.findAll();

    }




     // recuperer une commande par son id  //////////////////////////////////

    public CommandeEntity trouverParId(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID : " + id));
    }




    // MODIFIER une commande   ////////////////////////////////

    public CommandeEntity modifierCommande(Long id, CommandeDTO dto) {
        // 1. On vérifie si la commande existe
        CommandeEntity commandeExistante = trouverParId(id);

        // 2. On vérifie si le client associé au DTO existe
        ClientEntity client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + dto.getClientId()));

        // 3. Mise à jour des champs
        commandeExistante.setProduit(dto.getProduit());
        commandeExistante.setQuantite(dto.getQuantite());
        commandeExistante.setClient(client); // On peut changer le client de la commande

        // 4. Sauvegarde
        return commandeRepository.save(commandeExistante);
    }



    // SUPPRIMER une commande /////////////////////////////////////////

    public void supprimerCommande(Long id) {
        // Vérification d'existence
        CommandeEntity commande = trouverParId(id);
        commandeRepository.delete(commande);
    }

}
