package com.jts.movie.services;

import com.jts.movie.entities.PaymentCard;
import com.jts.movie.repositories.PaymentCardRepository;
import com.jts.movie.repositories.UserRepository;
import com.jts.movie.request.PaymentCardRequest;
import com.jts.movie.response.PaymentCardResponse;
import com.jts.movie.config.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jts.movie.entities.User;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentCardService {

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @Autowired
    private UserRepository userRepository;  // Inject the UserRepository to work with User entities

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Autowired
    private UserService userService;

    // Save payment card with encryption
    public void savePaymentCard(Long userId, PaymentCardRequest cardRequest) throws Exception {
        PaymentCard paymentCard = new PaymentCard();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // Encrypt sensitive card details
        paymentCard.setCardNumber(encryptionUtil.encrypt(cardRequest.getCardNumber()));
        paymentCard.setCardHolderName(encryptionUtil.encrypt(cardRequest.getCardHolderName()));
        paymentCard.setExpiryDate(encryptionUtil.encrypt(cardRequest.getExpiryDate()));
        paymentCard.setCvv(encryptionUtil.encrypt(cardRequest.getCvv()));

        paymentCard.setUser(user);

        // Save the payment card into the repository
        paymentCardRepository.save(paymentCard);
    }

    // Get a single payment card by its ID, decrypt and mask details before returning
    public PaymentCardResponse getPaymentCard(Long cardId) throws Exception {
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new Exception("Payment card not found"));

        PaymentCardResponse response = new PaymentCardResponse();
        response.setCardHolderName(encryptionUtil.decrypt(card.getCardHolderName()));
        response.setCardNumber(maskCardNumber(encryptionUtil.decrypt(card.getCardNumber())));  // Mask the card number
        response.setExpiryDate(encryptionUtil.decrypt(card.getExpiryDate()));
        response.setCvv("****");  // Never send CVV back in response

        return response;
    }

    // Get all payment cards for a specific user, mask card number before returning
    public List<PaymentCardResponse> getUserPaymentCards(Long userId) throws Exception {
        List<PaymentCard> cards = paymentCardRepository.findByUserId(userId);
        List<PaymentCardResponse> cardResponses = new ArrayList<>();

        for (PaymentCard card : cards) {
            PaymentCardResponse response = new PaymentCardResponse();
            response.setCardHolderName(encryptionUtil.decrypt(card.getCardHolderName()));
            response.setCardNumber(maskCardNumber(encryptionUtil.decrypt(card.getCardNumber())));  // Mask the card number
            response.setExpiryDate(encryptionUtil.decrypt(card.getExpiryDate()));

            // Do not include the CVV for security reasons
            response.setCvv("****");

            cardResponses.add(response);
        }
        return cardResponses;
    }

    // Update payment card details with encryption
    public void updatePaymentCard(Long cardId, PaymentCardRequest cardRequest) throws Exception {
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new Exception("Payment card not found"));

        // Encrypt updated sensitive details
        card.setCardNumber(encryptionUtil.encrypt(cardRequest.getCardNumber()));
        card.setCardHolderName(encryptionUtil.encrypt(cardRequest.getCardHolderName()));
        card.setExpiryDate(encryptionUtil.encrypt(cardRequest.getExpiryDate()));
        card.setCvv(encryptionUtil.encrypt(cardRequest.getCvv()));

        paymentCardRepository.save(card);
    }

    // Delete a payment card by its ID
    public void deletePaymentCard(Long cardId) throws Exception {
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new Exception("Payment card not found"));
        paymentCardRepository.delete(card);
    }

    // Utility method to mask card number (keep only last 4 digits visible)
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 4) {
            return "****";  // Too short to mask, return as is
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }

    public List<PaymentCard> getCardsForUser(String token) {
        Long userId = userService.getUserIdFromToken(token);
        return paymentCardRepository.findByUserId(userId);
    }
    public boolean validatePayment(PaymentCard card) {
        return card.getCardNumber().matches("\\d{16}")
                && card.getCvv().matches("\\d{3}")
                && card.getExpiryDate().matches("\\d{2}/\\d{2}")
                && card.getCardHolderName() != null;
    }
}
