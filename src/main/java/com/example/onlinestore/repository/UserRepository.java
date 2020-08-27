package com.example.onlinestore.repository;

import com.example.onlinestore.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
