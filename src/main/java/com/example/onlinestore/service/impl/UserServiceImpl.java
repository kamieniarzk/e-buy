package com.example.onlinestore.service.impl;

import com.example.onlinestore.auth.User;
import com.example.onlinestore.service.UserDaoService;
import com.example.onlinestore.repository.UserRepository;
import com.example.onlinestore.security.UserRole;
import com.example.onlinestore.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserDaoService userDaoService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDaoService userDaoService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userDaoService = userDaoService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String save(User user) {
        if(existsByUsername(user.getUsername()) && user.getId() != getByUsername(user.getUsername()).getId()) {
            return "Username already in use.";
        }
        if(user.getRole().equals("CUSTOMER")) {
            user.setGrantedAuthorities(UserRole.CUSTOMER.getGrantedAuthorities());
        } else {
            user.setGrantedAuthorities(UserRole.SELLER.getGrantedAuthorities());
        }
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userRepository.save(user);
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDaoService
                .selectUserByUsername(username)
                .orElseThrow(() ->
                new UsernameNotFoundException("Username " + username + "not found."));
    }

    public User getByUsername(String username) {
        return userRepository.findAll().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElseThrow(() ->
                                new UsernameNotFoundException("Username " + username + "not found."));
    }

    public boolean existsByUsername(String username) {
        return userRepository.findAll().stream()
                .anyMatch(user -> username.equals(user.getUsername()));
    }

}
