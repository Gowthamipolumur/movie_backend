package com.jts.movie.response;

import com.jts.movie.enums.gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String firstName;
    private String lastName;
    private Integer age;
    private gender gender;
    private String address;
    private String emailId;  // Added email field, useful for login or fetch user details
    private String roles;
    private String mobileNo;
    private String city;
    private String state;
    private String zipcode;
    private String token;   // Add token field for login response
    private String message; // Add message field for login response
    private Boolean promotionPreference;
    private List<PaymentCardResponse> paymentCards;
    // Custom constructor for login response
    public UserResponse(String emailId, String token, String message) {
        this.emailId = emailId;
        this.token = token;
        this.message = message;
    }
}
