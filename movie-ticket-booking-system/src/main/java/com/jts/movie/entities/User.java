package com.jts.movie.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import com.jts.movie.enums.gender;

import java.util.List;

@Entity
@Table(name = "USERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String firstName;
	private String lastName;

	private Integer age;

	private String address;

	private String billingAddress;

	@Enumerated(EnumType.STRING)
	private gender gender;

	@Size(max = 10, message = "Mobile number should not exceed 10 characters")
	private String mobileNo;

	@Column(unique = true, nullable = false)
	@Email
	private String emailId;

	@Column(nullable = false)
	@Size(min = 8)
	private String password;

	@Column(nullable = false)
	private String roles;

	@Column(nullable = false)
	private Boolean isActive;

	private String confirmationToken;

	private boolean promotionPreference;

	// One user can have at most 4 payment cards
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PaymentCard> paymentCards;
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void addPaymentCard(PaymentCard card) throws Exception {
		if (this.paymentCards.size() < 4) {
			this.paymentCards.add(card);
		} else {
			throw new Exception("Cannot store more than 4 payment cards");
		}
	}
}
