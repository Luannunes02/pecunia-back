package com.pecunia.pecunia.dto;

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
public class TransactionDTO {
  private String description;
  private BigDecimal amount;
  private LocalDateTime transactionDate;
  private String type;
  private LocalDateTime dueDate;
  private Boolean isPaid;
  private Boolean isRecurring;
  private String recurringFrequency;
  private Long accountId;
  private Long categoryId;
}