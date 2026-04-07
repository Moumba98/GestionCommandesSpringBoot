package com.example.gestion_clients.service;

import com.example.gestion_clients.dto.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public String createPaymentIntent(PaymentRequest paymentRequest) throws StripeException {
        // Stripe veut des centimes (ex: 10.50€ -> 1050)
        long amountInCents = (long) (paymentRequest.getAmount() * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("eur")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }
}