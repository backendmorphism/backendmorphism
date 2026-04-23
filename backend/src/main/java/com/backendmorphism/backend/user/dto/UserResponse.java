package com.backendmorphism.backend.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String role;

    private Boolean isActive;

    private LocalDateTime createdAt;
}