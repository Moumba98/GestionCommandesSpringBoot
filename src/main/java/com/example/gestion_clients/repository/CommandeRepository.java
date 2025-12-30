package com.example.gestion_clients.repository;

import com.example.gestion_clients.model.CommandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;


public interface CommandeRepository extends JpaRepository<CommandeEntity, Long> {

    List<CommandeEntity> findByClientId(Long clientId);

}
