package com.pecunia.pecunia.repository;

import com.pecunia.pecunia.entity.Transaction;
import com.pecunia.pecunia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query("select t from Transaction t join fetch t.category where t.account.user = :user and t.transactionDate between :start and :end order by t.transactionDate desc")
  List<Transaction> findByAccount_UserAndTransactionDateBetweenOrderByTransactionDateDescWithCategory(
      @Param("user") User user,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);
}