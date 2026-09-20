package com.pulsefit.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulsefit.auth.dto.AuthResponse;
import com.pulsefit.auth.dto.LoginRequest;
import com.pulsefit.auth.dto.LoginResponse;
import com.pulsefit.auth.dto.RegisterRequest;
import com.pulsefit.auth.entity.User;
import com.pulsefit.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

import com.pulsefit.auth.exception.InvalidCredentialsException;
import com.pulsefit.auth.exception.UserAlreadyExistsException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/admin-test")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("ADMIN authorization is working");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("JWT authentication is working");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest request) {

        User user = authService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        AuthResponse response = new AuthResponse(
                "User registered successfully",
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {

        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        User user = authService.getUserByUsername(
                request.getUsername()
        );

        LoginResponse response = new LoginResponse(
                "Login successful",
                token,
                user.getUsername(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<AuthResponse> handleUserAlreadyExists(
            UserAlreadyExistsException e) {

        AuthResponse response = new AuthResponse(
                e.getMessage(),
                null,
                null,
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<AuthResponse> handleInvalidCredentials(
            InvalidCredentialsException e) {

        AuthResponse response = new AuthResponse(
                e.getMessage(),
                null,
                null,
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthResponse> handleValidationException(
            MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        AuthResponse response = new AuthResponse(
                message,
                null,
                null,
                null
        );

        return ResponseEntity.badRequest().body(response);
    }
}