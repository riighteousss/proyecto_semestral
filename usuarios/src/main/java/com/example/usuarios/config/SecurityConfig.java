package com.example.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
   @Bean
    public PasswordEncoder encriptardatos() {
    return new BCryptPasswordEncoder();
   }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // opcional, para pruebas con Postman y PUT, POST
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}