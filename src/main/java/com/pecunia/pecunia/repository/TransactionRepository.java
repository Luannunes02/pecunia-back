package com.pecunia.pecunia.repository;

import com.pecunia.pecunia.entity.Transaction;
import com.pecunia.pecunia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByAccount_UserOrderByTransactionDateDesc(User user);

  List<Transaction> findByAccount_UserAndTransactionDateBetweenOrderByTransactionDateDesc(
      User user,
      LocalDateTime startDate,
      LocalDateTime endDate);
}