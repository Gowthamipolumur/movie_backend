package com.jts.movie.services;

import com.jts.movie.entities.Promotion;
import com.jts.movie.entities.User;
import com.jts.movie.entities.UserPromo;
import com.jts.movie.repositories.PromotionRepository;
import com.jts.movie.repositories.UserPromoRepository;
import com.jts.movie.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPromoRepository userPromoRepository;

    @Autowired
    private EmailService emailService;

    // Method to add a new promotion
    @Transactional
    public Promotion addPromotion(Promotion promotion) {
        // Set the validity status when saving
        promotion.updateValidity();

        // Save the promotion to the database
        Promotion savedPromotion = promotionRepository.save(promotion);

        // Return the saved promotion
        return savedPromotion;
    }


    // Method to send a promotion to all subscribed users
    public void sendPromotion(Long promotionId) throws Exception {
        // Retrieve the promotion by ID or throw an exception if not found
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new Exception("Promotion not found with ID: " + promotionId));

        // Check if the promotion has already been sent
        if (promotion.isSent()) {
            throw new Exception("Promotion has already been sent and cannot be modified.");
        }

        // Fetch all active users who have opted in for promotions
        List<User> subscribedUsers = userRepository.findByIsActive(true).stream()
                .filter(User::getPromotionPreference)
                .collect(Collectors.toList());

        // Send the promotion email to each subscribed user
        for (User user : subscribedUsers) {
            emailService.sendPromotionEmail(user.getEmailId(), promotion.getTitle(), promotion.getDescription());
        }

        // Update the promotion status to 'sent' and set the send date to the current date
        promotion.setSent(true);
        promotion.setSendDate(new java.sql.Date(System.currentTimeMillis()));

        // Save the updated promotion to the database
        promotionRepository.save(promotion);

        // Fetch all active users with promotion preference set to true
        List<User> eligibleUsers = userRepository.findByIsActive(true).stream()
                .filter(User::getPromotionPreference) // Only users who opted in for promotions
                .collect(Collectors.toList());

        // Create a `UserPromo` entry for each eligible user
        List<UserPromo> userPromos = eligibleUsers.stream().map(user ->
                UserPromo.builder()
                        .userToken(user.getEmailId()) // Use email ID as user token
                        .promo(promotion) // Link the promo
                        .isUsed(false)
                        .promoName(promotion.getPromoName())// Promo starts as unused
                        .build()
        ).collect(Collectors.toList());

        // Save all UserPromo entries in bulk
        userPromoRepository.saveAll(userPromos);
    }

    // Method to get all promotions with updated validity
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        // Update validity for each promotion before returning
        promotions.forEach(Promotion::updateValidity);
        return promotions;
    }

    // Method to get a single promotion by ID with updated validity
    public Optional<Promotion> getPromotionById(Long promoId) {
        Optional<Promotion> promotion = promotionRepository.findById(promoId);
        promotion.ifPresent(Promotion::updateValidity);
        return promotion;
    }

    // Method to delete a promotion by ID
    public void deletePromotion(Long promoId) {
        promotionRepository.deleteById(promoId);
    }

    @Transactional
    public Map<String, Object> checkPromo(String userToken, String promoCode) {
        Map<String, Object> response = new HashMap<>();

        // Fetch promo by title
        Optional<Promotion> promoOptional = promotionRepository.findByTitle(promoCode);

        // Check if promo exists and is valid
        if (promoOptional.isEmpty() || !promoOptional.get().getIsValid()) {
            response.put("isValid", false);
            response.put("message", "Promo code is not valid or expired.");
            return response;
        }

        // Retrieve the promotion object
        Promotion promotion = promoOptional.get();

        // Fetch user promo details
        Optional<UserPromo> userPromo = userPromoRepository.findByUserTokenAndPromoName(userToken, promoCode);

        if (userPromo.isPresent()) {
            UserPromo promo = userPromo.get(); // Get the UserPromo object from Optional
            if (Boolean.TRUE.equals(promo.getIsUsed())) {  // Check if the promo has already been used
                response.put("isValid", false);
                response.put("message", "Promo code has already been used by this user.");
                return response;
            }
        }


        // Valid promo code
        response.put("isValid", true);
        response.put("discountPercentage", promotion.getDiscountPercentage());
        response.put("message", "Promo code is valid.");
        return response;
    }

    public void syncPromotionsToUserPromo() {
        // Fetch all promotions from the Promotion table
        List<Promotion> promotions = promotionRepository.findAll();

        for (Promotion promotion : promotions) {
            // Check if the promo already exists in UserPromo, if not, create a new one
            List<UserPromo> existingUserPromos = userPromoRepository.findByPromoName(promotion.getPromoName());

            if (existingUserPromos.isEmpty()) {
                // Create new UserPromo for each user (assuming promo applies to all users initially)
                List<User> users = userRepository.findAll(); // Assuming a UserRepository exists

                for (User user : users) {
                    UserPromo userPromo = new UserPromo();
                    userPromo.setPromoName(promotion.getPromoName());
                    userPromo.setUserToken(user.getEmailId()); // Assuming emailId as user token
                    userPromo.setPromoName(promotion.getPromoName());
                    userPromo.setIsUsed(false);  // Initially promo code is not used
                    userPromoRepository.save(userPromo); // Save to the UserPromo table
                }
            }
        }
    }
}
