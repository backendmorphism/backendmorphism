package com.backendmorphism.backend.auth.service;

import com.backendmorphism.backend.auth.dto.AuthResponse;
import com.backendmorphism.backend.auth.dto.LoginRequest;
import com.backendmorphism.backend.auth.dto.SignupRequest;
import com.backendmorphism.backend.auth.jwt.JwtService;
import com.backendmorphism.backend.user.entity.User;
import com.backendmorphism.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse("Email already exists", null);
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // encrypted password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return new AuthResponse("User registered successfully", null);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                "Login successful",
                token
        );
    }
}