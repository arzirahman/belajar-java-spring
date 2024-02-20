package com.project.ordermakanan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordermakanan.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
