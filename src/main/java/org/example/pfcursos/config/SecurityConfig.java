package org.example.pfcursos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Necesario para que funcionen POST, PUT y DELETE
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite todo sin contraseña
                );
        return http.build();
    }
}
