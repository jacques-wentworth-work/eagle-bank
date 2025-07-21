package com.eaglebank.repository;

import com.eaglebank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findTopByIdStartingWithOrderByIdDesc(String id);
}
