package com.jts.movie.services;


import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    // Method to process payment (dummy)
    public boolean processPayment(String creditCardNumber, double amount) {
        // Integrate with payment gateway API here (e.g., Stripe or PayPal)
        // For demo, we'll assume payment is successful
        return true;
    }
}

