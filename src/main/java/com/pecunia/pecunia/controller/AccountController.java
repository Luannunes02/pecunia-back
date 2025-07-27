package com.pecunia.pecunia.controller;

import com.pecunia.pecunia.dto.request.AccountRequest;
import com.pecunia.pecunia.entity.Account;
import com.pecunia.pecunia.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "Endpoints para gerenciamento de contas")
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  @Operation(summary = "Criar uma nova conta")
  public ResponseEntity<Account> createAccount(
      @Valid @RequestBody AccountRequest request,
      Authentication authentication) {
    Account account = accountService.createAccount(request, authentication.getName());
    return ResponseEntity.ok(account);
  }

  @GetMapping
  @Operation(summary = "Listar todas as contas do usuário")
  public ResponseEntity<List<Account>> getAccounts(Authentication authentication) {
    List<Account> accounts = accountService.getAccounts(authentication.getName());
    return ResponseEntity.ok(accounts);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar uma conta")
  public ResponseEntity<Account> updateAccount(
      @PathVariable Long id,
      @Valid @RequestBody AccountRequest request,
      Authentication authentication) {
    Account account = accountService.updateAccount(id, request, authentication.getName());
    return ResponseEntity.ok(account);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir uma conta")
  public ResponseEntity<Void> deleteAccount(
      @PathVariable Long id,
      Authentication authentication) {
    accountService.deleteAccount(id, authentication.getName());
    return ResponseEntity.noContent().build();
  }
}