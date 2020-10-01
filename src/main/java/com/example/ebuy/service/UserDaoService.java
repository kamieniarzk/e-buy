package com.example.ebuy.service;

import com.example.ebuy.model.User;

import java.util.Optional;

public interface UserDaoService {
    Optional<User> selectUserByUsername(String username);
}
