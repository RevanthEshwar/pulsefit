package com.pulsefit.auth.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.pulsefit.auth.entity.User;
import com.pulsefit.auth.repository.UserRepository;

@Component
public class StaffInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public StaffInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {

        if (!userRepository.existsByUsername("staff")) {

            User staff = new User(
                    "staff",
                    "staff@pulsefit.com",
                    passwordEncoder.encode("Staff@123"),
                    "STAFF",
                    LocalDateTime.now()
            );

            userRepository.save(staff);

            System.out.println("Default STAFF user created successfully");
        }
    }
}