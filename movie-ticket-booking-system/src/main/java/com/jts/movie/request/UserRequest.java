package com.jts.movie.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class UserRequest {

	@NotBlank(message = "First name is mandatory")
	private String firstName;

	@NotBlank(message = "Last name is mandatory")
	private String lastName;

	@Email(message = "Email is invalid")
	@NotBlank(message = "Email is mandatory")
	private String emailId;


	@NotBlank(message = "Password is mandatory")
	private String password;

	@NotBlank(message = "Mobile number is mandatory")
	private String mobileNo;

	@NotBlank(message = "Address is mandatory")
	private String address;

	@NotBlank(message = "City is mandatory")
	private String city;

	@NotBlank(message = "State is mandatory")
	private String state;

	@NotBlank(message = "Zipcode is mandatory")
	private String zipcode;

	@Min(value = 1, message = "Age should be a positive number")
	private Integer age;

	private String gender;

	private String roles;

	private boolean promotionPreference;


	// New fields for password reset functionality
	private String resetToken;

	@NotBlank(message = "New password is mandatory")
	private String newPassword;

	// New field for payment card details (optional)
	private List<PaymentCardRequest> paymentCards;

}
