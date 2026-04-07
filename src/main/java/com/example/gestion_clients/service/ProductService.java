package com.example.gestion_clients.service;

import com.example.gestion_clients.model.Product;
import com.example.gestion_clients.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));
    }
}

