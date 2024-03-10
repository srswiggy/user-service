package com.swiggy.userservice.responses;

import com.swiggy.userservice.entities.Location;
import com.swiggy.userservice.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String username;
    private String role;
    private Location location;
}
