package com.pecunia.pecunia.service;

import com.pecunia.pecunia.dto.TransactionDTO;
import com.pecunia.pecunia.entity.Account;
import com.pecunia.pecunia.entity.Category;
import com.pecunia.pecunia.entity.Transaction;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.AccountRepository;
import com.pecunia.pecunia.repository.CategoryRepository;
import com.pecunia.pecunia.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Transactional
  public Transaction createTransaction(TransactionDTO transactionDTO, User user) {
    Transaction transaction = new Transaction();
    transaction.setDescription(transactionDTO.getDescription());
    transaction.setAmount(transactionDTO.getAmount());
    transaction.setTransactionDate(transactionDTO.getTransactionDate());
    transaction.setType(transactionDTO.getType());
    transaction.setDueDate(transactionDTO.getDueDate());
    transaction.setIsPaid(transactionDTO.getIsPaid());
    transaction.setIsRecurring(transactionDTO.getIsRecurring());
    transaction.setRecurringFrequency(transactionDTO.getRecurringFrequency());

    Account account = accountRepository.findById(transactionDTO.getAccountId())
        .orElseThrow(() -> new RuntimeException("Account not found"));
    transaction.setAccount(account);

    Category category = categoryRepository.findById(transactionDTO.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    transaction.setCategory(category);

    // Update account balance
    updateAccountBalance(account, transactionDTO.getAmount(), transactionDTO.getType());

    return transactionRepository.save(transaction);
  }

  public List<Transaction> getTransactionsByUser(User user) {
    return transactionRepository.findByAccount_UserOrderByTransactionDateDesc(user);
  }

  public List<Transaction> getTransactionsByUserAndDateRange(User user, LocalDateTime startDate,
      LocalDateTime endDate) {
    return transactionRepository.findByAccount_UserAndTransactionDateBetweenOrderByTransactionDateDesc(user, startDate,
        endDate);
  }

  @Transactional
  public Transaction updateTransaction(Long id, TransactionDTO transactionDTO, User user) {
    Transaction transaction = transactionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction not found"));

    // Verify if the transaction belongs to the user
    if (!transaction.getAccount().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Transaction does not belong to user");
    }

    // Update account balance (reverse old transaction and apply new one)
    updateAccountBalance(transaction.getAccount(), transaction.getAmount().negate(), transaction.getType());
    updateAccountBalance(transaction.getAccount(), transactionDTO.getAmount(), transactionDTO.getType());

    transaction.setDescription(transactionDTO.getDescription());
    transaction.setAmount(transactionDTO.getAmount());
    transaction.setTransactionDate(transactionDTO.getTransactionDate());
    transaction.setType(transactionDTO.getType());
    transaction.setDueDate(transactionDTO.getDueDate());
    transaction.setIsPaid(transactionDTO.getIsPaid());
    transaction.setIsRecurring(transactionDTO.getIsRecurring());
    transaction.setRecurringFrequency(transactionDTO.getRecurringFrequency());

    Account account = accountRepository.findById(transactionDTO.getAccountId())
        .orElseThrow(() -> new RuntimeException("Account not found"));
    transaction.setAccount(account);

    Category category = categoryRepository.findById(transactionDTO.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    transaction.setCategory(category);

    return transactionRepository.save(transaction);
  }

  @Transactional
  public void deleteTransaction(Long id, User user) {
    Transaction transaction = transactionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction not found"));

    // Verify if the transaction belongs to the user
    if (!transaction.getAccount().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Transaction does not belong to user");
    }

    // Update account balance (reverse the transaction)
    updateAccountBalance(transaction.getAccount(), transaction.getAmount().negate(), transaction.getType());

    transactionRepository.delete(transaction);
  }

  private void updateAccountBalance(Account account, BigDecimal amount, String type) {
    if ("EXPENSE".equals(type)) {
      account.setBalance(account.getBalance().subtract(amount));
    } else if ("INCOME".equals(type)) {
      account.setBalance(account.getBalance().add(amount));
    }
    accountRepository.save(account);
  }
}