package com.example.gestion_clients.service;

import com.example.gestion_clients.dto.CommandeDTO;
import com.example.gestion_clients.model.ClientEntity;
import com.example.gestion_clients.model.CommandeEntity;
import com.example.gestion_clients.repository.ClientRepository;
import com.example.gestion_clients.repository.CommandeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CommandeService commandeService;

    // 1. TEST : AJOUTER UNE COMMANDE
    @Test
    void testAjouterCommande() {
        // ARRANGE
        CommandeDTO dto = new CommandeDTO();
        dto.setClientId(1L);
        dto.setProduit("Ordinateur");
        dto.setQuantite(2);

        ClientEntity fauxClient = new ClientEntity();
        fauxClient.setId(1L);

        CommandeEntity commandeSimulee = CommandeEntity.builder()
                .produit("Ordinateur")
                .client(fauxClient)
                .build();

        // On  La recherche du client, et  La sauvegarde de la commande
        when(clientRepository.findById(1L)).thenReturn(Optional.of(fauxClient));
        when(commandeRepository.save(any(CommandeEntity.class))).thenReturn(commandeSimulee);

        // ACT
        CommandeEntity resultat = commandeService.ajouterCommande(dto);

        // ASSERT
        assertNotNull(resultat);
        assertEquals("Ordinateur", resultat.getProduit());
        verify(clientRepository).findById(1L);
        verify(commandeRepository).save(any(CommandeEntity.class));
    }

    // 2. TEST : TROUVER PAR ID
    @Test
    void testTrouverParId() {
        CommandeEntity commande = new CommandeEntity();
        commande.setId(10L);

        when(commandeRepository.findById(10L)).thenReturn(Optional.of(commande));

        CommandeEntity resultat = commandeService.trouverParId(10L);

        assertNotNull(resultat);
        assertEquals(10L, resultat.getId());
    }

    // 3. TEST : SUPPRIMER UNE COMMANDE
    @Test
    void testSupprimerCommande() {
        // ARRANGE
        Long id = 10L;
        CommandeEntity commande = new CommandeEntity();
        commande.setId(id);

        when(commandeRepository.findById(id)).thenReturn(Optional.of(commande));

        // ACT
        commandeService.supprimerCommande(id);

        // ASSERT
        verify(commandeRepository, times(1)).delete(commande);
    }
}
