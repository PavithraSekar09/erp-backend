package com.example.erp.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role;

    // Profile Fields
    private String name;
    private String phone;
    private String address;
    private String photo;
    private String department;

    // ✅ Admin managed
    private Double salary;
}