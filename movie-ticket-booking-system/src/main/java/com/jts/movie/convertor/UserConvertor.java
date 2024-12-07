package com.jts.movie.convertor;

import com.jts.movie.entities.User;
import com.jts.movie.request.UserRequest;
import com.jts.movie.response.UserResponse;

public class UserConvertor {

    public static User userDtoToUser(UserRequest userRequest, String encryptedPassword) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .emailId(userRequest.getEmailId())
                .mobileNo(userRequest.getMobileNo())
                .address(userRequest.getAddress())
                .city(userRequest.getCity())
                .state(userRequest.getState())
                .zipcode(userRequest.getZipcode())
                .password(encryptedPassword)
                .roles(userRequest.getRoles())
                .isActive(false)
                .promotionPreference(userRequest.isPromotionPreference())
                .build();
    }

    public static UserResponse userToUserDto(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailId(user.getEmailId())
                .mobileNo(user.getMobileNo())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zipcode(user.getZipcode())
                .promotionPreference(user.getPromotionPreference())
                .build();
    }
}
