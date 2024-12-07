package com.jts.movie.entities;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(unique = true, nullable = false)
	@Email(message = "Email is invalid")
	private String emailId;

	@Size(max = 10, message = "Mobile number should not exceed 10 characters")
	@Column(nullable = false)
	private String mobileNo;

	@Size(min = 8, message = "Password must be at least 8 characters")
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String state;

	@Column(nullable = false)
	private String zipcode;

	@Column(nullable = false)
	private String roles;

	private Integer age;

	@Column(nullable = false)
	private Boolean isActive;

	private String confirmationToken;

	@Column(nullable = false)
	private Boolean promotionPreference;

	// Add the resetToken field
	private String resetToken;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PaymentCard> paymentCards;

	public void addPaymentCard(PaymentCard card) {
		if (paymentCards.size() < 4) {
			paymentCards.add(card);
			card.setUser(this);
		} else {
			throw new RuntimeException("Cannot store more than 4 payment cards");
		}
	}
}
