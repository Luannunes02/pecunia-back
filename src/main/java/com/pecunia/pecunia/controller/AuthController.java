package com.pecunia.pecunia.controller;

import com.pecunia.pecunia.dto.request.LoginRequest;
import com.pecunia.pecunia.dto.request.UserRegistrationRequest;
import com.pecunia.pecunia.dto.response.AuthResponse;
import com.pecunia.pecunia.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "APIs para autenticação e registro de usuários")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @Operation(summary = "Registrar novo usuário", description = "Endpoint para criar uma nova conta de usuário")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  @Operation(summary = "Autenticar usuário", description = "Endpoint para autenticar um usuário existente")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}