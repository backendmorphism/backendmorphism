package com.backendmorphism.backend.auth.controller;

import com.backendmorphism.backend.auth.dto.AuthResponse;
import com.backendmorphism.backend.auth.dto.LoginRequest;
import com.backendmorphism.backend.auth.dto.SignupRequest;
import com.backendmorphism.backend.auth.service.AuthService;
import com.backendmorphism.backend.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response =
                authService.signup(request);
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Signup successful")
                .data(response)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse>  login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response =
                authService.login(request);

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }
}
