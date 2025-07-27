package com.pecunia.pecunia.service;

import com.pecunia.pecunia.dto.LoginRequest;
import com.pecunia.pecunia.dto.LoginResponse;
import com.pecunia.pecunia.dto.request.UserRegistrationRequest;
import com.pecunia.pecunia.dto.response.AuthResponse;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.UserRepository;
import com.pecunia.pecunia.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @Transactional
  public AuthResponse register(UserRegistrationRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email já cadastrado");
    }

    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setIsActive(true);

    user = userRepository.save(user);

    // Criar autenticação para o usuário recém-registrado
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()));

    String token = tokenProvider.generateToken(authentication);
    return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail());
  }

  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);

    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    return new LoginResponse(jwt, "Bearer", user.getId(), user.getName(), user.getEmail());
  }
}