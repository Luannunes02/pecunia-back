package com.pecunia.pecunia.controller;

import com.pecunia.pecunia.dto.TransactionDTO;
import com.pecunia.pecunia.entity.Transaction;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.service.TransactionService;
import com.pecunia.pecunia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Endpoints para gerenciamento de transações")
public class TransactionController {

  private final TransactionService transactionService;
  private final UserService userService;

  @PostMapping
  @Operation(summary = "Criar uma nova transação")
  public ResponseEntity<Transaction> createTransaction(
      @Valid @RequestBody TransactionDTO transactionDTO,
      @RequestHeader("Authorization") String token) {
    User user = userService.getUserFromToken(token);
    return ResponseEntity.ok(transactionService.createTransaction(transactionDTO, user));
  }

  @GetMapping
  @Operation(summary = "Listar todas as transações do usuário")
  public ResponseEntity<List<Transaction>> getTransactions(
      @RequestHeader("Authorization") String token) {
    User user = userService.getUserFromToken(token);
    return ResponseEntity.ok(transactionService.getTransactionsByUser(user));
  }

  @GetMapping("/filter")
  @Operation(summary = "Listar transações por período")
  public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
      @RequestParam LocalDateTime startDate,
      @RequestParam LocalDateTime endDate,
      @RequestHeader("Authorization") String token) {
    User user = userService.getUserFromToken(token);
    return ResponseEntity.ok(transactionService.getTransactionsByUserAndDateRange(user, startDate, endDate));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar uma transação")
  public ResponseEntity<Transaction> updateTransaction(
      @PathVariable Long id,
      @Valid @RequestBody TransactionDTO transactionDTO,
      @RequestHeader("Authorization") String token) {
    User user = userService.getUserFromToken(token);
    return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO, user));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir uma transação")
  public ResponseEntity<Void> deleteTransaction(
      @PathVariable Long id,
      @RequestHeader("Authorization") String token) {
    User user = userService.getUserFromToken(token);
    transactionService.deleteTransaction(id, user);
    return ResponseEntity.noContent().build();
  }
}