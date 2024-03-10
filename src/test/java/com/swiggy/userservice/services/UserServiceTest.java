package com.swiggy.userservice.services;

import com.swiggy.userservice.entities.Location;
import com.swiggy.userservice.entities.Role;
import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.repositories.RoleRepository;
import com.swiggy.userservice.repositories.UserRepository;
import com.swiggy.userservice.requests.UserRequest;
import com.swiggy.userservice.responses.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    @Mock
    private UserRepository userRepo;

    @Mock
    RoleRepository roleRepo;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(new User(1, "user1", "password", new Role(1, "CUSTOMER"), new Location()), new User(2, "user2", "password", new Role(2, "ADMIN"), new Location()));
        when(userRepo.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void createUser() throws Exception {
        UserRequest userRequest = new UserRequest("newUser", "pass", new Location(0,0));
        User user = new User(1, "newUser", encoder.encode("pass"), new Role(1, "CUSTOMER"), new Location());
        Role role = new Role(1, "CUSTOMER");

        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepo.findByName("CUSTOMER")).thenReturn(Optional.of(role));
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.create(userRequest, "CUSTOMER");

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
    }

    @Test
    void getUserById() {
        User user = new User(1, "user1", "password", new Role(1, "CUSTOMER"), new Location());
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = userService.get(1L);

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
    }

    @Test
    void userIsOwner() {
        User user = new User(1, "user1", "password", new Role(1, "CUSTOMER"), new Location());
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        Boolean isOwner = userService.isUserOwner("user1", 1L);

        assertTrue(isOwner);
    }

    @Test
    void userIsNotOwner() {
        User user = new User(1, "user1", "password", new Role(1, "USER"), new Location());
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        Boolean isOwner = userService.isUserOwner("user2", 1L);

        assertFalse(isOwner);
    }

    @Test
    void canAccessCatalogueAsAdmin() {
        User user = new User(1, "admin", "password", new Role(2, "ADMIN"), new Location());
        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(user));

        Boolean canAccess = userService.canAccessCatalogue("admin");

        assertTrue(canAccess);
    }

    @Test
    void cannotAccessCatalogueAsUser() {
        User user = new User(1, "user", "password", new Role(1, "CUSTOMER"), new Location());
        when(userRepo.findByUsername("user")).thenReturn(Optional.of(user));

        Boolean canAccess = userService.canAccessCatalogue("user");

        assertFalse(canAccess);
    }

}