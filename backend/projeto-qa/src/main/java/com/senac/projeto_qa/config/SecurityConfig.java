package com.senac.projeto_qa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // Rotas públicas
                .requestMatchers("/usuarios/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                // .anyRequest().permitAll())
                // Rotas com autenticação
                .anyRequest().authenticated())
                // Aqui estou forçando ele a usar o userDetailService
                .userDetailsService(userDetailsService) 
                .httpBasic(httpBasic -> {
                })

                .csrf(csrf -> csrf.disable());
        return http.build();
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    };

};
