package com.example.gestion_clients.service;

import com.example.gestion_clients.dto.ClientDTO;
import com.example.gestion_clients.model.ClientEntity;
import com.example.gestion_clients.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    // 1. TEST : TROUVER PAR ID
    @Test
    void testTrouverParId() {
        ClientEntity fauxClient = new ClientEntity();
        fauxClient.setId(1L);
        fauxClient.setNom("VDE");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(fauxClient));

        ClientEntity resultat = clientService.trouverParId(1L);

        assertNotNull(resultat);
        assertEquals("VDE", resultat.getNom());
    }

    // 2. TEST : AJOUTER UN CLIENT
    @Test
    void testAjouterClient() {
        // ARRANGE
        ClientDTO dto = new ClientDTO();
        dto.setNom("Amine");
        dto.setEmail("amine@example.com");

        ClientEntity clientSimule = ClientEntity.builder()
                .nom("Amine")
                .email("amine@example.com")
                .build();

        // On simule le comportement du save
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientSimule);

        // ACT
        ClientEntity resultat = clientService.ajouterClient(dto);

        // ASSERT
        assertNotNull(resultat);
        assertEquals("Amine", resultat.getNom());
        verify(clientRepository, times(1)).save(any(ClientEntity.class));
    }

    // 3. TEST : LISTER TOUS LES CLIENTS
    @Test
    void testListerClients() {
        // ARRANGE
        List<ClientEntity> clients = Arrays.asList(new ClientEntity(), new ClientEntity());
        when(clientRepository.findAll()).thenReturn(clients);

        // ACT
        List<ClientEntity> resultat = clientService.listerClients();

        // ASSERT
        assertEquals(2, resultat.size());
        verify(clientRepository, times(1)).findAll();
    }

    // 4. TEST : MODIFIER UN CLIENT
    @Test
    void testModifierClient() {
        // ARRANGE
        Long id = 1L;
        ClientDTO dto = new ClientDTO();
        dto.setNom("NomModifie");
        dto.setEmail("modifie@test.com");

        ClientEntity clientExistant = new ClientEntity();
        clientExistant.setId(id);
        clientExistant.setNom("AncienNom");

        when(clientRepository.findById(id)).thenReturn(Optional.of(clientExistant));
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // ACT
        ClientEntity resultat = clientService.modifierClient(id, dto);

        // ASSERT
        assertEquals("NomModifie", resultat.getNom());
        verify(clientRepository).save(clientExistant);
    }

    // 5. TEST : SUPPRIMER UN CLIENT
    @Test
    void testSupprimerClient() {

        // ARRANGE
        Long id = 1L;
        ClientEntity clientExistant = new ClientEntity();
        clientExistant.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(clientExistant));

        // ACT
        clientService.supprimerClient(id);

        // ASSERT
        verify(clientRepository, times(1)).delete(clientExistant);
    }
}