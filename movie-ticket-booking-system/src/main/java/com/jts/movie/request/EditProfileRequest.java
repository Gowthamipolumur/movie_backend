package com.jts.movie.request;

import lombok.Data;
import com.jts.movie.entities.PaymentCard; // Ensure this line is present

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class EditProfileRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String billingAddress;

    private String mobileNo;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    @Size(min = 8)
    private String password;

    private boolean promotionPreference;

    private List<PaymentCard> paymentCards;  // Add payment cards here
}
