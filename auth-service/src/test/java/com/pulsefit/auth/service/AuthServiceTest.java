package com.pulsefit.auth.service;

import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pulsefit.auth.entity.User;
import com.pulsefit.auth.repository.UserRepository;
import com.pulsefit.auth.security.JWTService;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pulsefit.auth.exception.UserAlreadyExistsException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pulsefit.auth.exception.InvalidCredentialsException;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegisterSuccess() {

        User user = new User(
                "revanth",
                "revanth@gmail.com",
                "encodedPassword",
                "USER",
                LocalDateTime.now()
        );

        when(userRepository.existsByUsername("revanth"))
                .thenReturn(false);

        when(userRepository.existsByEmail("revanth@gmail.com"))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        User result = authService.register(
                "revanth",
                "revanth@gmail.com",
                "password123"
        );

        assertNotNull(result);
        assertEquals("revanth", result.getUsername());
        assertEquals("revanth@gmail.com", result.getEmail());
        assertEquals("USER", result.getRole());
    }
    
    @Test
    public void testRegisterDuplicateUsername() {

        when(userRepository.existsByUsername("revanth"))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(
                        "revanth",
                        "newemail@gmail.com",
                        "password123"
                )
        );
    }
    
    @Test
    public void testRegisterDuplicateEmail() {

        when(userRepository.existsByUsername("anotheruser"))
                .thenReturn(false);

        when(userRepository.existsByEmail("test@gmail.com"))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(
                        "anotheruser",
                        "test@gmail.com",
                        "password123"
                )
        );
    }
    
    @Test
    public void testLoginSuccess() {

        String encodedPassword =
                new BCryptPasswordEncoder()
                        .encode("password123");

        User user = new User(
                "revanth",
                "revanth@gmail.com",
                encodedPassword,
                "USER",
                LocalDateTime.now()
        );

        when(userRepository.findByUsername("revanth"))
                .thenReturn(java.util.Optional.of(user));

        when(jwtService.generateToken("revanth", "USER"))
                .thenReturn("mock-jwt-token");

        String result = authService.login(
                "revanth",
                "password123"
        );

        assertNotNull(result);
        assertEquals("mock-jwt-token", result);
    }
    
    @Test
    public void testLoginWrongPassword() {

        String encodedPassword =
                new BCryptPasswordEncoder()
                        .encode("password123");

        User user = new User(
                "revanth",
                "revanth@gmail.com",
                encodedPassword,
                "USER",
                LocalDateTime.now()
        );

        when(userRepository.findByUsername("revanth"))
                .thenReturn(java.util.Optional.of(user));

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(
                        "revanth",
                        "wrongpassword"
                )
        );
    }
    
    @Test
    public void testLoginUserNotFound() {

        when(userRepository.findByUsername("unknown"))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(
                        "unknown",
                        "password123"
                )
        );
    }
    
    @Test
    public void testRegisterCallsRepositorySave() {

        when(userRepository.existsByUsername("newuser"))
                .thenReturn(false);

        when(userRepository.existsByEmail("newuser@gmail.com"))
                .thenReturn(false);

        User user = new User(
                "newuser",
                "newuser@gmail.com",
                "encodedPassword",
                "USER",
                LocalDateTime.now()
        );

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        authService.register(
                "newuser",
                "newuser@gmail.com",
                "password123"
        );

        verify(userRepository).save(any(User.class));
    }
    
}