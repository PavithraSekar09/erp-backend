package com.example.erp.user.service;

import com.example.erp.user.entity.User;
import com.example.erp.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE USER (REGISTER)
    public User createUser(User user) {

        // prevent duplicate email
        if (repo.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 🔥 IMPORTANT: SET ROLE
        user.setRole("ROLE_EMPLOYEE");

        return repo.save(user);
    }
}