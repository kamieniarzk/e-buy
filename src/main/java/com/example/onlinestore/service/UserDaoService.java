package com.example.onlinestore.service;

import com.example.onlinestore.model.User;

import java.util.Optional;

public interface UserDaoService {
    Optional<User> selectUserByUsername(String username);
}
