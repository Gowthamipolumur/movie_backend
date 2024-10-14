package com.jts.movie.convertor;

import com.jts.movie.entities.User;
import com.jts.movie.request.UserRequest;
import com.jts.movie.response.UserResponse;

public class UserConvertor {

    public static User userDtoToUser(UserRequest userRequest, String password) {
        User user = User.builder()
                .firstName(userRequest.getFirstName()) // Assuming UserRequest has separate first and last name fields
                .lastName(userRequest.getLastName())
                .age(userRequest.getAge())
                .address(userRequest.getAddress())
                .gender(userRequest.getGender())
                .mobileNo(userRequest.getMobileNo())
                .emailId(userRequest.getEmailId())
                .roles(userRequest.getRoles())
                .password(password)
                .build();

        return user;
    }

    public static UserResponse userToUserDto(User user) {
        UserResponse userResponse = UserResponse.builder()
                .firstName(user.getFirstName()) // Assuming UserResponse has separate fields for first and last names
                .lastName(user.getLastName())
                .age(user.getAge())
                .address(user.getAddress())
                .gender(user.getGender())
                .build();

        return userResponse;
    }
}
