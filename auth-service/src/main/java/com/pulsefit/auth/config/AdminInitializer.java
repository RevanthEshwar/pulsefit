package com.pulsefit.auth.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.pulsefit.auth.entity.User;
import com.pulsefit.auth.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {

        if (!userRepository.existsByUsername("admin")) {

            User admin = new User(
                    "admin",
                    "admin@pulsefit.com",
                    passwordEncoder.encode("Admin@123"),
                    "ADMIN",
                    LocalDateTime.now()
            );

            userRepository.save(admin);

            System.out.println("Default ADMIN user created successfully");
        }
    }
}