package com.example.gestion_clients.service;

import com.example.gestion_clients.dto.CommandeDTO;
import com.example.gestion_clients.model.UserEntity; // Import User au lieu de Client
import com.example.gestion_clients.model.CommandeEntity;
import com.example.gestion_clients.model.Product;
import com.example.gestion_clients.repository.UserRepository; // Nouveau Repository
import com.example.gestion_clients.repository.CommandeRepository;
import com.example.gestion_clients.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private UserRepository userRepository; // On utilise User maintenant

    @Autowired
    private ProductRepository productRepository;

    // 1. AJOUTER
    public CommandeEntity ajouterCommande(CommandeDTO dto) {
        UserEntity user = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        return commandeRepository.save(CommandeEntity.builder()
                .product(product)
                .quantite(dto.getQuantite())
                .user(user) // Assure-toi que le champ dans CommandeEntity s'appelle 'user'
                .prixUnitaire(product.getPrice())
                .dateCommande(LocalDateTime.now())
                .reference("REF-" + System.currentTimeMillis())
                .build());
    }

    // 2. TROUVER PAR ID (C'est la méthode qui te manque !)
    public CommandeEntity trouverParId(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID : " + id));
    }

    // 3. HISTORIQUE
    public List<CommandeEntity> getHistoriqueParClient(String username) {
        return commandeRepository.findByUserUsernameOrderByDateCommandeDesc(username);
    }

    // 4. MODIFIER
    public CommandeEntity modifierCommande(Long id, CommandeDTO dto) {
        // Ici on appelle trouverParId(id) qui est définie juste au-dessus
        CommandeEntity commandeExistante = trouverParId(id);

        UserEntity user = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        commandeExistante.setProduct(product);
        commandeExistante.setQuantite(dto.getQuantite());
        commandeExistante.setUser(user);

        return commandeRepository.save(commandeExistante);
    }

    // 5. SUPPRIMER
    public void supprimerCommande(Long id) {
        CommandeEntity commande = trouverParId(id);
        commandeRepository.delete(commande);
    }

    public List<CommandeEntity> listerToutesLesCommandes() {
        return commandeRepository.findAll();
    }
}