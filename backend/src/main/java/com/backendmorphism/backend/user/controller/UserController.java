package com.backendmorphism.backend.user.controller;

import com.backendmorphism.backend.common.dto.ApiResponse;
import com.backendmorphism.backend.user.dto.UserResponse;
import com.backendmorphism.backend.user.entity.User;
import com.backendmorphism.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> currentUser(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        UserResponse response = userService.mapToUserResponse(user);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User fetched successfully")
                .data(response)
                .build();
    }
}