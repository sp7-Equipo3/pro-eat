package com.example.spring.config;

import com.example.spring.enums.Role;
import com.example.spring.models.User;
import com.example.spring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        Optional<User> aux = userRepository.findByUsername("admin");

        // Caso 1: No existe usuario "admin" → crear
        if (aux.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin123@"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Initial admin created: username=admin, password=Admin123@");
            return;
        }

        // Caso 2: Existe "admin" pero NO existe ningún ADMIN → corregir
        User admin = aux.get();
        if (!userRepository.existsByRole(Role.ADMIN)) {
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Existing 'admin' user role updated to ADMIN.");
        }
    }
}
