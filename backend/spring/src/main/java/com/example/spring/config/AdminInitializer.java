package com.example.spring.config;

import com.example.spring.enums.Role;
import com.example.spring.models.User;
import com.example.spring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("Admin123@"));
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        System.out.println("Initial admin created: username=admin, password=Admin123@");
    }
}
