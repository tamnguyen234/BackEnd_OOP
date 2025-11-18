// package com.javaproject.Backend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class UnSecurityConfig {

// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// // Tắt CSRF và cho phép truy cập MỌI THỨ
// http.csrf().disable()
// .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

// return http.build();
// }
// }