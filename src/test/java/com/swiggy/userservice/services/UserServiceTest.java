package com.swiggy.userservice.services;

import com.swiggy.userservice.entities.Role;
import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.repositories.UserRepository;
import com.swiggy.userservice.responses.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    @Mock
    UserRepository userRepo;
    @InjectMocks
    UserService userService;
    @Mock
    User user;
    @BeforeEach
    void setUp(){
        openMocks(this);
    }
    @Test
    void test_getAllReturnsListofUserResponse() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepo.findAll()).thenReturn(users);
        when(users.get(0).getRole()).thenReturn(new Role());

        List<UserResponse> response = userService.getAll();

        verify(userRepo, times(1)).findAll();
        assertNotNull(response);
    }
}