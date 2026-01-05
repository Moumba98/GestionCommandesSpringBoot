package com.example.gestion_clients.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Un client peut avoir plusieurs commandes

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<CommandeEntity> commandes;
}
