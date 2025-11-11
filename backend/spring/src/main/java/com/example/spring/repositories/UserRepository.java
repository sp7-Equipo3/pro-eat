package com.example.spring.repositories;

import com.example.spring.enums.Role;
import com.example.spring.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByRole(Role role);

    @Query("""
    SELECT u FROM User u
    WHERE
        (:username IS NULL OR :username = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND
        (:role IS NULL OR CAST(:role AS string) = '' OR u.role = :role)
""")
    Page<User> searchUsers(
            @Param("username") String username,
            @Param("role") Role role,
            Pageable pageable
    );


    UUID id(UUID id);

    boolean existsById( UUID responsibleId);
}
