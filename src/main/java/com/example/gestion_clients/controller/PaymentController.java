package com.example.gestion_clients.controller;

import com.example.gestion_clients.dto.PaymentRequest;
import com.example.gestion_clients.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-intent")
    public Map<String, String> createIntent(@RequestBody PaymentRequest request) throws StripeException {
        String clientSecret = paymentService.createPaymentIntent(request);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", clientSecret);
        return response;
    }
}
