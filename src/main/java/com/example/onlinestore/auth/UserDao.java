package com.example.onlinestore.auth;

import java.util.Optional;

public interface UserDao {
    Optional<User> selectUserByUsername(String username);
}
