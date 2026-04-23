package com.imir.backend.controller;

import com.imir.backend.dto.request.LoginRequestDto;
import com.imir.backend.dto.request.RegisterRequestDto;
import com.imir.backend.dto.response.LoginResponseDto;
import com.imir.backend.dto.response.UserResponseDto;
import com.imir.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public LoginResponseDto register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return authService.register(requestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return authService.login(requestDto);
    }

    @GetMapping("/me")
    public UserResponseDto me(Authentication authentication) {
        return authService.getCurrentUser(authentication.getName());
    }
}