package com.example.gestion_clients.repository;

import com.example.gestion_clients.model.CommandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<CommandeEntity, Long> {

    // On cherche par le username de l'entité User (qui est liée au champ 'user')
    List<CommandeEntity> findByUserUsernameOrderByDateCommandeDesc(String username);

    // On cherche par l'ID de l'utilisateur
    List<CommandeEntity> findByUserId(Long userId);

    // Pour retrouver les produits d'une même commande
    List<CommandeEntity> findByReference(String reference);

}
