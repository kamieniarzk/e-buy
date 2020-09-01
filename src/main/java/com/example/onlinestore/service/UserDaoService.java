package com.example.onlinestore.service;

import com.example.onlinestore.auth.User;
import com.example.onlinestore.auth.UserDao;
import com.example.onlinestore.repository.UserRepository;
import com.example.onlinestore.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoService implements UserDao {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserDaoService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
//        userRepository.save(new User("michaelscott", passwordEncoder.encode("password"), true, true, true, true, UserRole.ADMIN.getGrantedAuthorities()));
//        userRepository.save(new User("user", passwordEncoder.encode("password"), true, true, true, true, UserRole.CUSTOMER.getGrantedAuthorities()));

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
