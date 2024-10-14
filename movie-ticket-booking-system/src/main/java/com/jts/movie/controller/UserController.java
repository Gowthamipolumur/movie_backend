package com.jts.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import jakarta.validation.Valid;
import java.security.Principal;
import com.jts.movie.config.JWTService;
import com.jts.movie.entities.User;
import com.jts.movie.request.UserRequest;
import com.jts.movie.services.UserService;
import com.jts.movie.repositories.UserRepository;
import com.jts.movie.request.EditProfileRequest;
import java.util.List; // Import for List
import com.jts.movie.entities.PaymentCard; // Import for PaymentCard


import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private UserRepository userRepository;

	// User registration endpoint
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequest userRequest) {
		try {
			String message = userService.addUser(userRequest);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// User email confirmation endpoint
	@PostMapping("/confirmRegistration")
	public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String token) {
		Optional<User> userOptional = userRepository.findByConfirmationToken(token);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setIsActive(true); // Activate the user account
			user.setConfirmationToken(null); // Clear the confirmation token
			userRepository.save(user);
			return ResponseEntity.ok("User account successfully confirmed.");
		} else {
			return ResponseEntity.badRequest().body("Invalid confirmation token.");
		}
	}

	// User login endpoint
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody UserRequest userRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getEmailId(), userRequest.getPassword()));

			if (authentication.isAuthenticated()) {
				String token = jwtService.generateToken(userRequest.getEmailId());
				return new ResponseEntity<>(token, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	// User logout endpoint
	@PostMapping("/logout")
	public ResponseEntity<String> logoutUser(Principal principal) {
		try {
			// Add logic to handle user logout (e.g., invalidate JWT or session)
			return new ResponseEntity<>("User logged out successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Edit profile endpoint
	@PutMapping("/editProfile")
	public ResponseEntity<String> editUserProfile(@RequestBody @Valid EditProfileRequest editProfileRequest, Principal principal) {
		try {
			String email = principal.getName();
			Optional<User> userOptional = userRepository.findByEmailId(email);

			if (userOptional.isPresent()) {
				User user = userOptional.get();

				// Update user information from the editProfileRequest
				user.setFirstName(editProfileRequest.getFirstName());
				user.setLastName(editProfileRequest.getLastName());
				user.setBillingAddress(editProfileRequest.getBillingAddress());
				user.setPassword(editProfileRequest.getPassword()); // Ensure password is encrypted
				user.setPromotionPreference(editProfileRequest.isPromotionPreference());

				// Check if the user can add more cards
				List<PaymentCard> newPaymentCards = editProfileRequest.getPaymentCards();
				if (newPaymentCards != null && !newPaymentCards.isEmpty()) {
					for (PaymentCard card : newPaymentCards) {
						try {
							user.addPaymentCard(card);
						} catch (Exception e) {
							return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
						}
					}
				}

				userRepository.save(user);
				return ResponseEntity.ok("Profile updated successfully.");
			} else {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
