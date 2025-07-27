package com.pecunia.pecunia.service;

import com.pecunia.pecunia.dto.request.AccountRequest;
import com.pecunia.pecunia.entity.Account;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.AccountRepository;
import com.pecunia.pecunia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final UserRepository userRepository;

  @Transactional
  public Account createAccount(AccountRequest request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Account account = new Account();
    account.setName(request.getName());
    account.setType(request.getType());
    account.setBalance(request.getInitialBalance());
    account.setInitialBalance(request.getInitialBalance());
    account.setIsActive(true);
    account.setUser(user);

    return accountRepository.save(account);
  }

  public List<Account> getAccounts(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    return accountRepository.findByUser(user);
  }

  @Transactional
  public Account updateAccount(Long id, AccountRequest request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

    if (!account.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Conta não pertence ao usuário");
    }

    account.setName(request.getName());
    account.setType(request.getType());
    account.setInitialBalance(request.getInitialBalance());

    return accountRepository.save(account);
  }

  @Transactional
  public void deleteAccount(Long id, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

    if (!account.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Conta não pertence ao usuário");
    }

    account.setIsActive(false);
    accountRepository.save(account);
  }
}