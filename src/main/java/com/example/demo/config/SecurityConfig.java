package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Static files & Swagger
            .requestMatchers("/", "/index.html", "/favicon.ico", "/js/**", "/css/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
            // Auth endpoints
            .requestMatchers("/users/login", "/users/register").permitAll()
            // Books authorization
            .requestMatchers(HttpMethod.POST, "/books").hasRole("LIBRARIAN")
            .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("LIBRARIAN")
            .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("LIBRARIAN")
            .requestMatchers(HttpMethod.GET, "/books", "/books/available").authenticated()
            // Borrow authorization
            .requestMatchers(HttpMethod.GET, "/borrow/records").hasRole("LIBRARIAN")
            .requestMatchers(HttpMethod.GET, "/borrow/history/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/borrow/**").authenticated()
            // User authorization
            .requestMatchers(HttpMethod.GET, "/users").hasRole("LIBRARIAN")
            // Rest of endpoints
            .anyRequest().authenticated()
        );

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}