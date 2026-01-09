package com.example.gestion_clients.service;

import com.example.gestion_clients.dto.ClientDTO;
import com.example.gestion_clients.model.ClientEntity;
import com.example.gestion_clients.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired

    private ClientRepository clientRepository;


    public ClientEntity ajouterClient(ClientDTO clientDTO) {
        // On transforme le DTO en Entity

        ClientEntity client = ClientEntity.builder()
                .nom(clientDTO.getNom())
                .email(clientDTO.getEmail())
                .build();

        return clientRepository.save(client);
    }

    // afficher tous les clients

    public List<ClientEntity> listerClients() {
        return clientRepository.findAll();
    }

    // recuperer un client par son id

    public ClientEntity trouverParId(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + id));
    }


    // MODIFIER un client

    public ClientEntity modifierClient(Long id, ClientDTO clientDTO) {
        // 1. On cherche le client existant
        ClientEntity clientExistant = trouverParId(id);

        // 2. On met à jour les champs avec les données du DTO
        clientExistant.setNom(clientDTO.getNom());
        clientExistant.setEmail(clientDTO.getEmail());

        // 3. On sauvegarde les modifications
        return clientRepository.save(clientExistant);
    }


    // suprimer un client


    public void supprimerClient(Long id) {
        // On vérifie s'il existe avant de supprimer pour éviter une erreur SQL
        ClientEntity client = trouverParId(id);
        clientRepository.delete(client);
    }


}
