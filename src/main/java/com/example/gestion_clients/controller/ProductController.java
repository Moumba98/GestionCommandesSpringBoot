package com.example.gestion_clients.controller;

import com.example.gestion_clients.model.Product;
import com.example.gestion_clients.repository.ProductRepository;
import com.example.gestion_clients.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // 1. VOIR TOUS LES PRODUITS (Public)
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }



    // Récupérer un produit par son ID
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // 3. AJOUTER UN PRODUIT (Admin uniquement - sera protégé par SecurityConfig)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 4. SUPPRIMER UN PRODUIT (Admin uniquement)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}