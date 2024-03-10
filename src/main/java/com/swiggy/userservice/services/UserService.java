package com.swiggy.userservice.services;

import com.swiggy.userservice.common.exceptions.NoSuchRoleException;
import com.swiggy.userservice.entities.Role;
import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.mappers.UserMapper;
import com.swiggy.userservice.repositories.RoleRepository;
import com.swiggy.userservice.repositories.UserRepository;
import com.swiggy.userservice.requests.UserRequest;
import com.swiggy.userservice.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    PasswordEncoder encoder;

    public List<UserResponse> getAll() {
        List<User> users = userRepo.findAll();

        return users.stream()
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse create(UserRequest userRequest, String type) throws Exception {
        System.out.println(userRequest.getLocation());
        User user = UserMapper.fromUserRequest(userRequest, encoder.encode(userRequest.getPassword()), userRequest.getLocation());
        Role role = roleRepo.findByName(type.toUpperCase()).orElseThrow(NoSuchRoleException::new);
        user.setRole(role);
        User res = userRepo.save(user);
        return UserMapper.toUserResponse(res);
    }

    public UserResponse get(long userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new UsernameNotFoundException(""));
        return UserMapper.toUserResponse(user);
    }

    public Boolean isUserOwner(String username, long userid) {
        User user = userRepo.findById(userid).orElseThrow(()->new UsernameNotFoundException("username not found"));

        return user.getUsername().equals(username);
    }

    public Boolean canAccessCatalogue(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("username not found"));
        return user.getRole().getName().equals("ADMIN");
    }
}
