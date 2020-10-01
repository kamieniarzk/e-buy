package com.example.ebuy.repository;

import com.example.ebuy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
