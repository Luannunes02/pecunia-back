package com.pecunia.pecunia.service;

import com.pecunia.pecunia.dto.TransactionDTO;
import com.pecunia.pecunia.dto.response.AccountResponseDTO;
import com.pecunia.pecunia.dto.response.CategoryResponseDTO;
import com.pecunia.pecunia.dto.response.TransactionResponseDTO;
import com.pecunia.pecunia.entity.Account;
import com.pecunia.pecunia.entity.Category;
import com.pecunia.pecunia.entity.Transaction;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.AccountRepository;
import com.pecunia.pecunia.repository.CategoryRepository;
import com.pecunia.pecunia.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final CategoryRepository categoryRepository;

  @Transactional
  public Transaction createTransaction(TransactionDTO dto, User user) {
    Transaction transaction = new Transaction();
    mapDtoToTransaction(dto, transaction);

    transaction.setUser(user);

    Account account = accountRepository.findById(dto.getAccountId())
        .orElseThrow(() -> new RuntimeException("Account not found"));
    transaction.setAccount(account);

    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    transaction.setCategory(category);

    updateAccountBalance(account, dto.getAmount(), dto.getType());

    return transactionRepository.save(transaction);
  }

  @Transactional(readOnly = true)
  public List<TransactionResponseDTO> getTransactionsByUser(User user) {
    return transactionRepository.findByAccount_UserOrderByTransactionDateDesc(user)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<TransactionResponseDTO> getTransactionsByUserAndDateRange(User user, LocalDateTime start,
      LocalDateTime end) {
    return transactionRepository.findByAccount_UserAndTransactionDateBetweenOrderByTransactionDateDesc(user, start, end)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  @Transactional
  public Transaction updateTransaction(Long id, TransactionDTO dto, User user) {
    Transaction transaction = transactionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction not found"));

    if (!transaction.getAccount().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Transaction does not belong to user");
    }

    // Reverte o saldo antigo
    updateAccountBalance(transaction.getAccount(), transaction.getAmount().negate(), transaction.getType());

    // Atualiza campos
    mapDtoToTransaction(dto, transaction);

    Account account = accountRepository.findById(dto.getAccountId())
        .orElseThrow(() -> new RuntimeException("Account not found"));
    transaction.setAccount(account);

    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    transaction.setCategory(category);

    // Aplica o saldo novo
    updateAccountBalance(account, dto.getAmount(), dto.getType());

    return transactionRepository.save(transaction);
  }

  @Transactional
  public void deleteTransaction(Long id, User user) {
    Transaction transaction = transactionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction not found"));

    if (!transaction.getAccount().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Transaction does not belong to user");
    }

    updateAccountBalance(transaction.getAccount(), transaction.getAmount().negate(), transaction.getType());

    transactionRepository.delete(transaction);
  }

  private void mapDtoToTransaction(TransactionDTO dto, Transaction transaction) {
    transaction.setDescription(dto.getDescription());
    transaction.setAmount(dto.getAmount());
    transaction.setTransactionDate(dto.getTransactionDate());
    transaction.setType(dto.getType());
    transaction.setDueDate(dto.getDueDate());
    transaction.setIsPaid(dto.getIsPaid());
    transaction.setIsRecurring(dto.getIsRecurring());
    transaction.setRecurringFrequency(dto.getRecurringFrequency());
  }

  private TransactionResponseDTO toResponseDTO(Transaction transaction) {
    TransactionResponseDTO dto = new TransactionResponseDTO();
    dto.setId(transaction.getId());
    dto.setDescription(transaction.getDescription());
    dto.setAmount(transaction.getAmount());
    dto.setTransactionDate(transaction.getTransactionDate());
    dto.setType(transaction.getType());
    dto.setDueDate(transaction.getDueDate());
    dto.setIsPaid(transaction.getIsPaid());
    dto.setIsRecurring(transaction.getIsRecurring());
    dto.setRecurringFrequency(transaction.getRecurringFrequency());
    dto.setAccountId(transaction.getAccount().getId());
    dto.setCategoryId(transaction.getCategory().getId());

    AccountResponseDTO accountDTO = new AccountResponseDTO();
    accountDTO.setId(transaction.getAccount().getId());
    accountDTO.setName(transaction.getAccount().getName());
    accountDTO.setType(transaction.getAccount().getType());
    accountDTO.setBalance(transaction.getAccount().getBalance());
    dto.setAccount(accountDTO);

    CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
    categoryDTO.setId(transaction.getCategory().getId());
    categoryDTO.setName(transaction.getCategory().getName());
    categoryDTO.setType(transaction.getCategory().getType());
    dto.setCategory(categoryDTO);

    return dto;
  }

  private void updateAccountBalance(Account account, BigDecimal amount, String type) {
    if ("EXPENSE".equalsIgnoreCase(type)) {
      account.setBalance(account.getBalance().subtract(amount));
    } else if ("INCOME".equalsIgnoreCase(type)) {
      account.setBalance(account.getBalance().add(amount));
    }
    accountRepository.save(account);
  }
}
