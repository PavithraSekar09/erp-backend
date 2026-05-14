package com.example.erp.config;

import com.example.erp.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                )

                .authorizeHttpRequests(auth -> auth

                        // ✅ Allow preflight requests (VERY IMPORTANT)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // =========================
                        // PUBLIC ENDPOINTS
                        // =========================
                        .requestMatchers(
                                "/",
                                "/health",
                                "/api/auth/**",
                                "/h2-console/**",
                                "/uploads/**"
                        ).permitAll()

                        // =========================
                        // USERS
                        // =========================
                        .requestMatchers(
                                "/api/users/upload-photo",
                                "/api/users/me",
                                "/api/users/profile"
                        ).authenticated()

                        .requestMatchers(
                                "/api/users/create"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                "/api/users",
                                "/api/users/**"
                        ).hasAuthority("ROLE_ADMIN")

                        // =========================
                        // EMPLOYEE
                        // =========================
                        .requestMatchers(
                                "/api/employees/me"
                        ).authenticated()

                        .requestMatchers(
                                "/api/employees/**"
                        ).hasAuthority("ROLE_ADMIN")

                        // =========================
                        // FINANCE
                        // =========================
                        .requestMatchers(
                                "/api/admin-finance/**"
                        ).hasAuthority("ROLE_ADMIN")

                        // =========================
                        // SALES
                        // =========================
                        .requestMatchers(
                                "/api/sales/**"
                        ).authenticated()

                        // =========================
                        // PRODUCTS
                        // =========================
                        .requestMatchers(
                                "/api/products/**"
                        ).authenticated()

                        // =========================
                        // ATTENDANCE
                        // =========================
                        .requestMatchers(
                                "/api/attendance/all",
                                "/api/attendance/today"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                "/api/attendance/**"
                        ).authenticated()

                        // =========================
                        // ALL OTHER REQUESTS
                        // =========================
                        .anyRequest().authenticated()
                )

                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}