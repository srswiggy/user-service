package com.swiggy.userservice.mappers;

import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.responses.UserResponse;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole());

        return userResponse;
    }
}
