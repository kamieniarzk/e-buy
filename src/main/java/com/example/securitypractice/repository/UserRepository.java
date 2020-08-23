package com.example.securitypractice.repository;

import com.example.securitypractice.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
