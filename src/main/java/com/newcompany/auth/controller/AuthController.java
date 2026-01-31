package com.newcompany.auth.controller;

import com.newcompany.auth.model.JwtAuthenticationResponse;
import com.newcompany.auth.service.AuthenticationService;
import com.newcompany.auth.model.LoginRequest;
import com.newcompany.auth.model.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtAuthenticationResponse register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        JwtAuthenticationResponse jwtToken = authenticationService.register(request,response);
        return jwtToken;
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        JwtAuthenticationResponse jwtToken = authenticationService.login(request,response);
        return jwtToken;
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response){
        authenticationService.logout(response);
        return ResponseEntity.noContent().build();
    }

}
