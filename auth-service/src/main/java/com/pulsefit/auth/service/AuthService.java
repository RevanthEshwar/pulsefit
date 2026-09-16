package com.pulsefit.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pulsefit.auth.entity.User;
import com.pulsefit.auth.repository.UserRepository;
import com.pulsefit.auth.security.JWTService;

import com.pulsefit.auth.exception.InvalidCredentialsException;
import com.pulsefit.auth.exception.UserAlreadyExistsException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(
                    "Invalid username or password");
        }

        return jwtService.generateToken(
                user.getUsername(),
                user.getRole()
        );
    }

    public User register(String username, String email, String password) {

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(
                    "Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(
                    "Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(
                username,
                email,
                encodedPassword,
                "USER",
                LocalDateTime.now()
        );

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}