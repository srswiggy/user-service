package com.swiggy.userservice.services;

import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.mappers.UserMapper;
import com.swiggy.userservice.repositories.UserRepository;
import com.swiggy.userservice.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    public List<UserResponse> getAll() {
        List<User> users = userRepo.findAll();

        return users.stream()
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
    }
}
