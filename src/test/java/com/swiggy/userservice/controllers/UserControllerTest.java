package com.swiggy.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
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
    @Test
    void test_goAllCreatesUserSuccesfully() throws Exception {
        List<UserResponse> users = new ArrayList<>();
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user-management/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService, times(1)).getAll();
    }

    @Test
    void test_createCreatesAValidUserWithROle() throws Exception {
        UserResponse userResponse = new UserResponse(1, "user", "ADMIN");
        UserRequest userRequest = new UserRequest("user", "pass");
        when(userService.create(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user-management/users")
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)))
                .andDo(MockMvcResultHandlers.print());

        verify(userService, times(1)).create(any(UserRequest.class));
    }
}