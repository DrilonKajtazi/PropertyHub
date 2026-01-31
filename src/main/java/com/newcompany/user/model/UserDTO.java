package com.newcompany.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDTO {

    private String username;

    private String email;

    private Set<Role> authorities;
}
