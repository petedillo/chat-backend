package com.chatbackend.controller;

import com.chatbackend.dto.UserDto;
import com.chatbackend.dto.UserResponseDto;
import com.chatbackend.entity.User;
import com.chatbackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserDto userDto) {
        User user = authService.registerUser(userDto);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setRoles(user.getRoles());
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDto userDto) {
        try {
            Optional<User> user = authService.loginUser(userDto.getUsername(), userDto.getPassword());
            if (user.isPresent()) {
                String token = authService.generateJwtToken(user.get());
                UserResponseDto userResponseDto = new UserResponseDto();
                userResponseDto.setId(user.get().getId());
                userResponseDto.setUsername(user.get().getUsername());
                userResponseDto.setRoles(user.get().getRoles());
                return ResponseEntity.ok(Map.of("user", userResponseDto, "token", token));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred"));
        }
    }
}