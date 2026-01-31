package com.propertyhub.auth.model;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;

    private String password;
}
