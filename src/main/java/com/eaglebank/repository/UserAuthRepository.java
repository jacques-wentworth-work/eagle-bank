package com.eaglebank.repository;

import com.eaglebank.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
}
