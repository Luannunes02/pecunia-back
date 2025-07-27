package com.pecunia.pecunia.service;

import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.UserRepository;
import com.pecunia.pecunia.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
  }

  @Transactional(readOnly = true)
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
  }

  @Transactional(readOnly = true)
  public User getUserFromToken(String token) {
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    String email = jwtTokenProvider.getUsernameFromJWT(token);
    return findByEmail(email);
  }

  @Transactional
  public User save(User user) {
    return userRepository.save(user);
  }

  @Transactional
  public void delete(Long id) {
    userRepository.deleteById(id);
  }
}