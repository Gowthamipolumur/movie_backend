package com.jts.movie.services;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.jts.movie.entities.User;
import com.jts.movie.repositories.UserRepository;
import com.jts.movie.request.UserRequest;
import com.jts.movie.response.UserResponse;
import com.jts.movie.config.JWTService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserService {

	private static Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private JWTService jwtService;

	// Method to handle user registration
	public String addUser(UserRequest userRequest) throws MessagingException {
		if (userRepository.findByEmailId(userRequest.getEmailId()).isPresent()) {
			throw new IllegalArgumentException("User already exists");
		}

		// Encrypt password
		String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());

		// Create and save user in inactive status
		User user = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.age(userRequest.getAge())
				.address(userRequest.getAddress())
				.emailId(userRequest.getEmailId())
				.password(encryptedPassword)
				.roles(userRequest.getRoles())
				.isActive(false) // Default inactive until email confirmation
				.build();

		userRepository.save(user);

		// Send confirmation email
		sendConfirmationEmail(userRequest.getEmailId());

		return "User registered successfully. Please check your email for confirmation.";
	}

	// Method to send a confirmation email
	public void sendConfirmationEmail(String email) throws MessagingException {
		Optional<User> optionalUser = userRepository.findByEmailId(email);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// Generate a unique confirmation token
			String token = UUID.randomUUID().toString();
			// Construct confirmation URL
			String confirmationLink = "http://localhost:8080/user/confirmRegistration?token=" + token;

			// Build email content
			String subject = "Confirm Your Registration";
			String body = "<p>Hello " + user.getFirstName() + " " + user.getLastName() + ",</p>"
					+ "<p>Thank you for registering. Please click the link below to confirm your registration:</p>"
					+ "<a href=\"" + confirmationLink + "\">Confirm Registration</a>"
					+ "<p>If you did not register, please ignore this email.</p>";

			// Send the email
			sendEmail(user.getEmailId(), subject, body);

			// Save the token in the user's record for later verification
			user.setConfirmationToken(token);
			userRepository.save(user); // Save the token
		} else {
			throw new IllegalArgumentException("User with email " + email + " not found.");
		}
	}

	// Method to send emails
	public void sendEmail(String to, String subject, String body) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error("Error sending email to {}: {}", to, e.getMessage());
			throw new RuntimeException("Failed to send email. Please try again later.");
		}
	}

	// Method to confirm the user's registration
	public void confirmRegistration(String token) {
		User user = userRepository.findByConfirmationToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Invalid token"));
		user.setIsActive(true); // Activate user upon confirmation
		user.setConfirmationToken(null); // Clear the confirmation token
		userRepository.save(user);
	}

	// Method to handle user login
	public UserResponse loginUser(UserRequest userRequest) {
		log.info("User login attempt: {}", userRequest.getEmailId());

		// Find user by email
		Optional<User> userOptional = userRepository.findByEmailId(userRequest.getEmailId());
		if (!userOptional.isPresent()) {
			throw new IllegalArgumentException("Invalid email or password");
		}

		User user = userOptional.get();

		// Verify the password
		if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid email or password");
		}

		// Generate a JWT token if credentials are valid
		String token = jwtService.generateToken(user.getEmailId());

		// Return a response with the token and user info
		return new UserResponse(user.getEmailId(), token, "Login successful");
	}
}
