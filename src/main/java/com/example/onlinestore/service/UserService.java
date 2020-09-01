package com.example.onlinestore.service;

import com.example.onlinestore.auth.User;
import com.example.onlinestore.auth.UserDao;
import com.example.onlinestore.repository.UserRepository;
import com.example.onlinestore.security.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
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
        if(user.getImageUrl() == null) {
            user.setImageUrl("https://st3.depositphotos.com/6672868/13701/v/450/depositphotos_137014128-stock-illustration-user-profile-icon.jpg");
        }
        userRepository.save(user);
        return null;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao
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

    public void update(User user, long id) {
        userRepository.findById(id).ifPresent(userRepository::save);
    }
}
