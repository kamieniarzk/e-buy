package com.example.securitypractice.auth;

import com.example.securitypractice.repository.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.securitypractice.security.UserRole.*;

@Repository("fake")
public class UserDaoService implements UserDao {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserDaoService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        return getUsers().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    private List<User> getUsers() {
        return userRepository.findAll();
    }
}
