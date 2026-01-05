package com.example.gestion_clients.repository;

import com.example.gestion_clients.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<ClientEntity, Long>{

    ClientEntity findByEmail(String email);

}
