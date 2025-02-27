package com.chatbackend.service;

import com.chatbackend.dto.UserDto;
import com.chatbackend.entity.User;
import com.chatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserDto userDto) {
        if (userDto == null || userDto.getUsername() == null || userDto.getPassword() == null) {
            throw new IllegalArgumentException("UserDto and its fields must not be null");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(userDto.getRoles());
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user;
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserDetails(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username must not be null");
        }

        return userRepository.findByUsername(username);
    }
}