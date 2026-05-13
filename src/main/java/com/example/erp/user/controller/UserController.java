package com.example.erp.user.controller;

import com.example.erp.user.entity.User;
import com.example.erp.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // GET LOGGED IN USER
    // =========================
    @GetMapping("/me")
    public User getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return repo.findByEmail(email);
    }

    // =========================
    // UPDATE PROFILE (NO PHOTO)
    // =========================
    @PutMapping("/profile")
    public User updateProfile(Authentication authentication,
                              @RequestBody User updatedUser) {

        String email = authentication.getName();
        User user = repo.findByEmail(email);

        user.setName(updatedUser.getName());
        user.setDepartment(updatedUser.getDepartment());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());

        return repo.save(user);
    }

    // =========================
    // GET ALL EMPLOYEES
    // =========================
    @GetMapping("/employees")
    public List<User> getAllEmployees() {
        return repo.findByRole("EMPLOYEE");
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // =========================
    // DELETE USER
    // =========================
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repo.deleteById(id);
    }

    // =========================
    // CREATE USER (ADMIN)
    // =========================
    @PostMapping("/create")
    public User createUser(@RequestBody User user,
                           Authentication authentication) {

        User admin = repo.findByEmail(authentication.getName());

        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Only ADMIN can create users");
        }

        if (repo.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        user.setRole("EMPLOYEE");
        user.setPassword(passwordEncoder.encode("12345"));

        return repo.save(user);
    }
}