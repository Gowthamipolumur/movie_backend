package com.jts.movie.response;

import com.jts.movie.enums.gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String name;
    private Integer age;
    private gender gender;
    private String address;
}