package com.example.usuarios;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.usuarios.config.JwtAuthenticationFilter;
import com.example.usuarios.service.JwtUtil;

@TestConfiguration
public class TestSecurityConfig {

    // Solo override SecurityFilterChain para tests si es necesario
    @Bean
    @Primary
    public SecurityFilterChain testFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Usuarios en memoria SOLO para tests
    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password(testPasswordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();

        UserDetails user = User.builder()
            .username("testuser")
            .password(testPasswordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails tecnico = User.builder()
            .username("tecnico")
            .password(testPasswordEncoder().encode("tecnico123"))
            .roles("TECNICO")
            .build();

        return new InMemoryUserDetailsManager(admin, user, tecnico);
    }

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Solo si necesitas un JwtUtil específico para tests
    @Bean
    @Primary
    public JwtUtil testJwtUtil() {
        return new JwtUtil();
    }
}