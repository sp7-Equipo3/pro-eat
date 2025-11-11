package com.example.spring.models;

import com.example.spring.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable=false,  unique=true)
    private String username;

    @Column(nullable=false)
    private String password; // encriptado (BCrypt)

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;
}
