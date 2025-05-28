package com.pecunia.pecunia.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "goals")
public class Goal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal targetAmount;

  @Column(name = "current_amount", precision = 10, scale = 2)
  private BigDecimal currentAmount = BigDecimal.ZERO;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "target_date", nullable = false)
  private LocalDateTime targetDate;

  @Column(name = "is_completed")
  private Boolean isCompleted = false;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne
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