package com.swiggy.userservice.mappers;

import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.requests.UserRequest;
import com.swiggy.userservice.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


public class UserMapper {
    public static User fromUserRequest(UserRequest request, String encodedPass) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPass);
        return user;
    }

    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole().getName());

        return userResponse;
    }
}
