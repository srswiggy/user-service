package com.swiggy.userservice.controllers;

import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.requests.UserRequest;
import com.swiggy.userservice.responses.UserResponse;
import com.swiggy.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user-management/users")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> userResponses = userService.getAll();

        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest, @RequestHeader HttpHeaders headers) throws Exception{
        String userType = headers.getFirst("type");
        UserResponse userResponse = userService.create(userRequest, userType);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
