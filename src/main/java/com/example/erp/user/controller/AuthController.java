package com.example.erp.user.controller;

import com.example.erp.security.JwtUtil;
import com.example.erp.user.entity.User;
import com.example.erp.user.repository.UserRepository;
import com.example.erp.user.dto.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // 🔐 LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        System.out.println("==== LOGIN REQUEST ====");
        System.out.println("EMAIL: " + user.getEmail());
        System.out.println("INPUT PASSWORD: " + user.getPassword());

        User dbUser = repo.findByEmail(user.getEmail());

        if (dbUser == null) {
            System.out.println("❌ USER NOT FOUND");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        System.out.println("DB PASSWORD: " + dbUser.getPassword());

        boolean match = passwordEncoder.matches(
                user.getPassword(),
                dbUser.getPassword()
        );

        System.out.println("MATCH RESULT: " + match);

        if (!match) {
            System.out.println("❌ PASSWORD MISMATCH");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        System.out.println("✅ LOGIN SUCCESS");

        String token = jwtUtil.generateToken(
                dbUser.getEmail(),
                dbUser.getRole()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }

    // =========================
    // 📝 REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        System.out.println("==== REGISTER REQUEST ====");
        System.out.println("EMAIL: " + user.getEmail());

        User existing = repo.findByEmail(user.getEmail());

        if (existing != null) {
            System.out.println("❌ EMAIL ALREADY EXISTS");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email already registered");
        }

        // 👉 store as EMPLOYEE (as you wanted)
        user.setRole("EMPLOYEE");

        // encode password
        user.setPassword(passwordEncoder.encode("12345"));

        repo.save(user);

        System.out.println("✅ USER REGISTERED");

        return ResponseEntity.ok("User Registered Successfully");
    }
}