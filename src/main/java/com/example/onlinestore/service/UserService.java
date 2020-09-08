package com.example.onlinestore.service;

import com.example.onlinestore.auth.User;

public interface UserService {
    String save(User user);
    User getByUsername(String username);
    boolean existsByUsername(String username);
}
