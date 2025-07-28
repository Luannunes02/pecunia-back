package com.pecunia.pecunia.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
  private Long id;
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
  private AccountResponseDTO account;
  private CategoryResponseDTO category;
}
