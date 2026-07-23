package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.UnauthorizedOperationException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  public UserResponse register(RegisterRequest registerRequest) {
    if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use: " + registerRequest.getEmail());
    }

    User user = new User();
    user.setName(registerRequest.getName());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : Role.STUDENT);

    User savedUser = userRepository.save(user);
    logger.info("User Registered: ID={}, Email={}, Role={}", savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

    return convertToResponse(savedUser);
  }

  public LoginResponse login(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new UnauthorizedOperationException("Invalid email or password");
    }

    String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
    logger.info("User Login: ID={}, Email={}, Role={}", user.getId(), user.getEmail(), user.getRole());

    return new LoginResponse(token, convertToResponse(user));
  }

  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    return convertToResponse(user);
  }

  private UserResponse convertToResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole()
    );
  }
}