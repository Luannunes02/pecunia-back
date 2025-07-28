package com.pecunia.pecunia.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(name = "transaction_date", nullable = false)
  private LocalDateTime transactionDate;

  @Column(nullable = false)
  private String type; // INCOME, EXPENSE

  @Column(name = "due_date")
  private LocalDateTime dueDate;

  @Column(name = "is_paid")
  private Boolean isPaid = false;

  @Column(name = "is_recurring")
  private Boolean isRecurring = false;

  @Column(name = "recurring_frequency")
  private String recurringFrequency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  @JsonIgnoreProperties({ "transactions", "user" })
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  @JsonIgnoreProperties({ "transactions", "budgets" })
  private Category category;

  @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
  @JsonIgnoreProperties("transaction")
  private Attachment attachment;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

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