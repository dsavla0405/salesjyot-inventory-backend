package com.example.inventorygenius.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection only if you have other protection like SameSite cookies.
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers("/getClientInfo", "/public/**").permitAll() // Allow public routes without authentication.
                    .anyRequest().authenticated() // Protect all other endpoints.
            )
            .oauth2Login(oauth2 -> 

                oauth2.defaultSuccessUrl("http://localhost:3000/home", true) // Redirect on successful login.
            )   
            .sessionManagement(session -> 
                session.maximumSessions(1) // Limit to 1 session per user.
                      .maxSessionsPreventsLogin(false) // Allows new logins to invalidate older sessions.
            );
         
        return http.build();
    }
}


