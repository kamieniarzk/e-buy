package com.example.ebuy.service;

import com.example.ebuy.model.User;

public interface UserService {
    String save(User user);
    User getByUsername(String username);
    boolean existsByUsername(String username);
}
