package com.imir.backend.service;

import com.imir.backend.dto.request.LoginRequestDto;
import com.imir.backend.dto.request.RegisterRequestDto;
import com.imir.backend.dto.response.LoginResponseDto;
import com.imir.backend.dto.response.UserResponseDto;
import com.imir.backend.entity.User;
import com.imir.backend.exception.ValidationException;
import com.imir.backend.repository.UserRepository;
import com.imir.backend.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDto register(RegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ValidationException("Email already exists");
        }

        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser.getEmail());

        return new LoginResponseDto(token, mapToUserResponse(savedUser));
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ValidationException("Invalid email or password"));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ValidationException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new LoginResponseDto(token, mapToUserResponse(user));
    }

    public UserResponseDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("User not found"));

        return mapToUserResponse(user);
    }

    private UserResponseDto mapToUserResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}