package com.pecunia.pecunia.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String type; // CHECKING, SAVINGS, CREDIT_CARD, INVESTMENT

  @Column(nullable = false)
  private BigDecimal balance;

  @Column(name = "initial_balance")
  private BigDecimal initialBalance;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnoreProperties({ "accounts", "categories", "budgets", "goals" })
  private User user;

  @OneToMany(mappedBy = "account")
  @JsonIgnoreProperties("account")
  private List<Transaction> transactions;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}