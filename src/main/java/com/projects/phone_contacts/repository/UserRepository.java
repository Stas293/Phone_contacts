package com.projects.phone_contacts.repository;

import com.projects.phone_contacts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(final String username);

    boolean existsByUsernameIgnoreCase(final String username);
}