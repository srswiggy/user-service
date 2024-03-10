package com.swiggy.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.userservice.entities.Location;
import com.swiggy.userservice.entities.Role;
import com.swiggy.userservice.entities.User;
import com.swiggy.userservice.requests.UserRequest;
import com.swiggy.userservice.responses.UserResponse;
import com.swiggy.userservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private List<UserResponse> sampleUsers;

    @Test
    void test_goAllCreatesUserSuccesfully() throws Exception {
        List<UserResponse> users = new ArrayList<>();
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/user-management/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService, times(1)).getAll();
    }

    @Test
    public void testGetAllUsers_thenReturnNonEmptyList() throws Exception {
        sampleUsers = Arrays.asList(
                new UserResponse(1, "User One", "ADMIN", new Location()),
                new UserResponse(2, "User Two", "CUSTOMER", new Location())
        );

        given(userService.getAll()).willReturn(sampleUsers);

        mockMvc.perform(get("/user-management/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(sampleUsers.size()))
                .andExpect(jsonPath("$[0].username").value(sampleUsers.get(0).getUsername()))
                .andExpect(jsonPath("$[1].username").value(sampleUsers.get(1).getUsername()));
    }

    @Test
    public void testGetAllUsers_thenReturnEmptyList() throws Exception {
        sampleUsers = Arrays.asList();

        given(userService.getAll()).willReturn(sampleUsers);

        mockMvc.perform(get("/user-management/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(sampleUsers.size()));
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = {"ADMIN"})
    public void testGetUser_ReturnsUser() throws Exception {
        int userId = 1;
        UserResponse expectedResponse = new UserResponse(userId, "Test User", "ADMIN", new Location(0, 0));
        when(userService.get(userId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/user-management/users/{userid}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.username").value(expectedResponse.getUsername()))
                .andExpect(jsonPath("$.role").value(expectedResponse.getRole()));
        verify(userService, times(1)).get(userId);
    }

    @Test
    @WithMockUser(username = "user")
    public void testAuthenticateSuccess() throws Exception {
        mockMvc.perform(get("/user-management/users/authorize"))
                .andExpect(status().isOk());
    }
}