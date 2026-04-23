package com.backendmorphism.backend.user.service;

import com.backendmorphism.backend.user.dto.UserResponse;
import com.backendmorphism.backend.user.entity.User;
import com.backendmorphism.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    public UserResponse mapToUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
