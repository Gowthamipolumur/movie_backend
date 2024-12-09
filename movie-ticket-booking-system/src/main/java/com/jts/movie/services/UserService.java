package com.jts.movie.services;
import com.jts.movie.config.EncryptionUtil;
import com.jts.movie.response.PaymentCardResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;

import com.jts.movie.entities.User;
import com.jts.movie.entities.PaymentCard;
import com.jts.movie.repositories.UserRepository;
import com.jts.movie.repositories.PaymentCardRepository;
import com.jts.movie.request.UserRequest;
import com.jts.movie.request.PaymentCardRequest;
import com.jts.movie.request.ChangePasswordRequest;
import com.jts.movie.request.EditProfileRequest;
import com.jts.movie.response.UserResponse;
import com.jts.movie.config.JWTService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PaymentCardRepository paymentCardRepository;

	@Autowired
	private EncryptionUtil encryptionUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private JWTService jwtService;

	// Register user and send confirmation email
	public String addUser(UserRequest userRequest) throws MessagingException {
		// Check if the user already exists
		if (userRepository.findByEmailId(userRequest.getEmailId()).isPresent()) {
			throw new IllegalArgumentException("User already exists with email: " + userRequest.getEmailId());
		}

		// Encrypt password and save the user
		String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());
		User user = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.emailId(userRequest.getEmailId())
				.mobileNo(userRequest.getMobileNo())
				.password(encryptedPassword)
				.address(userRequest.getAddress())
				.city(userRequest.getCity())
				.state(userRequest.getState())
				.zipcode(userRequest.getZipcode())
				.roles(userRequest.getRoles())
				.age(userRequest.getAge())
				.isActive(false) // Initially inactive
				.promotionPreference(userRequest.isPromotionPreference())
				.build();

		// Generate and set the confirmation token
		String token = UUID.randomUUID().toString();
		user.setConfirmationToken(token);

		// Save the user
		userRepository.save(user);

		// Handle payment cards if provided
		if (userRequest.getPaymentCards() != null && !userRequest.getPaymentCards().isEmpty()) {
			if (userRequest.getPaymentCards().size() > 3) {
				throw new IllegalArgumentException("You can add a maximum of 3 payment cards.");
			}

			// Loop through each payment card and save it
			for (PaymentCardRequest cardRequest : userRequest.getPaymentCards()) {
				PaymentCard paymentCard = new PaymentCard();
				try {
					paymentCard.setCardNumber(encryptionUtil.encrypt(cardRequest.getCardNumber()));  // Encrypt card number
					paymentCard.setCardHolderName(cardRequest.getCardHolderName());  // No need to encrypt name
					paymentCard.setExpiryDate(encryptionUtil.encrypt(cardRequest.getExpiryDate()));  // Encrypt expiry date
					paymentCard.setCvv(encryptionUtil.encrypt(cardRequest.getCvv()));  // Encrypt CVV
				} catch (Exception e) {
					throw new RuntimeException("Error encrypting payment card details", e);
				}
				paymentCard.setUser(user);  // Associate the card with the user

				// Save payment card to repository
				paymentCardRepository.save(paymentCard);
			}
		}

		// Send confirmation email
		sendConfirmationEmail(user, token);

		return "User registered successfully. Please check your email for confirmation.";
	}

	// Send confirmation email
	private void sendConfirmationEmail(User user, String token) throws MessagingException {
		String confirmationLink = "http://localhost:8080/user/confirmRegistration?token=" + token;
		String subject = "Confirm Your Registration";
		String body = "<p>Hello " + user.getFirstName() + " " + user.getLastName() + ",</p>"
				+ "<p>Thank you for registering. Please click the link below to confirm your registration:</p>"
				+ "<a href=\"" + confirmationLink + "\">Confirm Registration</a>"
				+ "<p>If you did not register, please ignore this email.</p>";

		sendEmail(user.getEmailId(), subject, body);
	}


	private void sendUpdatedEmail(User user) throws MessagingException {
//		String confirmationLink = "http://localhost:8080/user/confirmRegistration?token=" + token;
		String subject = "Profile Updated";
		String body = "<p>Hello " + user.getFirstName() + " " + user.getLastName() + ",</p>"
				+ "<p>Your Profile updated successfully</p>";
//				+ "<a href=\"" + confirmationLink + "\">Confirm Registration</a>"
//				+ "<p>If you did not register, please ignore this email.</p>";

		sendEmail(user.getEmailId(), subject, body);
	}
	// Email sending logic
	private void sendEmail(String to, String subject, String body) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);
		mailSender.send(mimeMessage);
		log.info("Confirmation email sent to: {}", to);
	}

	// Confirm registration
	public void confirmRegistration(String token) {
		User user = userRepository.findByConfirmationToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Invalid token: " + token));

		user.setIsActive(true);
		user.setConfirmationToken(null);
		userRepository.save(user);
		log.info("User {} confirmed their registration.", user.getEmailId());
	}


	public void updateUserProfile(String currentUserEmail, EditProfileRequest editProfileRequest) throws MessagingException {
		// Find the user by the current logged-in email
		Optional<User> userOptional = userRepository.findByEmailId(currentUserEmail);

		if (userOptional.isEmpty()) {
			throw new IllegalArgumentException("User not found.");
		}

		User user = userOptional.get();

		// Update user profile information from the request
		user.setFirstName(editProfileRequest.getFirstName());
		user.setLastName(editProfileRequest.getLastName());
		user.setMobileNo(editProfileRequest.getMobileNo());
		user.setAddress(editProfileRequest.getAddress());
		user.setCity(editProfileRequest.getCity());
		user.setState(editProfileRequest.getState());
		user.setPromotionPreference(editProfileRequest.isPromotionPreference());
		user.setZipcode(editProfileRequest.getZipcode());

		// Handle Payment Information (if any)
		List<PaymentCard> paymentCards = editProfileRequest.getPaymentCards();
		if (paymentCards != null && !paymentCards.isEmpty()) {
			user.getPaymentCards().clear(); // Clear existing payment cards
			for (PaymentCard paymentCardRequest : paymentCards) {
				PaymentCard card = new PaymentCard();
				card.setCardNumber(paymentCardRequest.getCardNumber());
				card.setCardHolderName(paymentCardRequest.getCardHolderName());
				card.setExpiryDate(paymentCardRequest.getExpiryDate());
				card.setCvv(paymentCardRequest.getCvv());
				card.setUser(user); // Link the card to the user
				user.getPaymentCards().add(card); // Add new card
			}
		}

		// Save the updated user profile
		userRepository.save(user);
		sendUpdatedEmail(user);
	}

	public void changePassword(String currentUserEmail, ChangePasswordRequest changePasswordRequest) {
		// Find the user by the current logged-in email
		Optional<User> userOptional = userRepository.findByEmailId(currentUserEmail);

		if (userOptional.isEmpty()) {
			throw new IllegalArgumentException("User not found.");
		}

		User user = userOptional.get();

		// Check if the current password provided matches the user's existing password
		if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Current password is incorrect.");
		}

		// Ensure new password and confirm password match
		if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
			throw new IllegalArgumentException("New password and confirm password do not match.");
		}

		// Encrypt the new password and update it
		String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
		user.setPassword(encodedPassword);

		// Save the updated user with the new password
		userRepository.save(user);
	}

	public UserResponse getUserProfile(String email) {
		// Find the user by their email
		Optional<User> userOptional = userRepository.findByEmailId(email);

		if (userOptional.isEmpty()) {
			throw new IllegalArgumentException("User not found.");
		}

		User user = userOptional.get();

		// Create a response DTO (UserResponse) to send back the profile information
		UserResponse userResponse = new UserResponse();
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setEmailId(user.getEmailId());
		userResponse.setMobileNo(user.getMobileNo());
		userResponse.setAddress(user.getAddress());
		userResponse.setCity(user.getCity());
		userResponse.setRoles(user.getRoles());
		userResponse.setState(user.getState());
		userResponse.setZipcode(user.getZipcode());
		userResponse.setPromotionPreference(user.getPromotionPreference());

		// Fetch payment cards if any
		List<PaymentCard> paymentCards = user.getPaymentCards();
		List<PaymentCardResponse> paymentCardResponses = new ArrayList<>();

		for (PaymentCard card : paymentCards) {
			try {
				PaymentCardResponse cardResponse = new PaymentCardResponse(
						encryptionUtil.decrypt(card.getCardNumber()),  // Decrypt card number
						card.getCardHolderName(),  // Cardholder name is not encrypted
						encryptionUtil.decrypt(card.getExpiryDate()),  // Decrypt expiry date
						encryptionUtil.decrypt(card.getCvv())  // Decrypt CVV
				);
				paymentCardResponses.add(cardResponse);
			} catch (Exception e) {
				// Handle the decryption error, maybe log it or throw a custom exception
				log.error("Error decrypting payment card details for user {}: {}", user.getEmailId(), e.getMessage());
				throw new RuntimeException("Error decrypting payment card details.");
			}
		}

		userResponse.setPaymentCards(paymentCardResponses);

		return userResponse;
	}

	private static final String SECRET_KEY = "your_secret_key"; // Replace with your actual secret key

	// Method to get user ID from JWT token
	public Long getUserIdFromToken(String token) {
		// Parse the JWT token to extract the claims
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY) // Set the signing key to verify the token
				.parseClaimsJws(token) // Parse the JWT
				.getBody();

		// Extract the user ID from the claims (assuming it's stored as a claim)
		return Long.parseLong(claims.get("userId").toString()); // Adjust based on how user ID is stored in the token
	}

	public String getUserEmailById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
		return user.getEmailId();
	}

}
