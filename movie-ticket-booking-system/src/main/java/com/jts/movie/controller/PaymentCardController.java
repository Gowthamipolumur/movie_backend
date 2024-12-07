package com.jts.movie.controller;

import com.jts.movie.entities.PaymentCard;
import com.jts.movie.response.PaymentCardResponse;
import com.jts.movie.services.PaymentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-card")
public class PaymentCardController {

    @Autowired
    private PaymentCardService paymentCardService;

    @GetMapping("/{cardId}")
    public ResponseEntity<PaymentCardResponse> getPaymentCard(@PathVariable Long cardId) throws Exception {
        PaymentCardResponse response = paymentCardService.getPaymentCard(cardId);
        return ResponseEntity.ok(response);
    }
}
