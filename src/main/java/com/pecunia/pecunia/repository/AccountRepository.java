package com.pecunia.pecunia.repository;

import com.pecunia.pecunia.entity.Account;
import com.pecunia.pecunia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  List<Account> findByUser(User user);
}