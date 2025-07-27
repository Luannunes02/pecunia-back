package com.pecunia.pecunia.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {
  @NotBlank(message = "A descrição é obrigatória")
  private String description;

  @NotNull(message = "O valor é obrigatório")
  @Positive(message = "O valor deve ser positivo")
  private BigDecimal amount;

  @NotBlank(message = "O tipo é obrigatório")
  private String type; // INCOME, EXPENSE, TRANSFER

  @NotNull(message = "A data da transação é obrigatória")
  private LocalDateTime transactionDate;

  private LocalDateTime dueDate;

  private Boolean isPaid = true;

  private Boolean isRecurring = false;

  private String recurringFrequency; // DAILY, WEEKLY, MONTHLY, YEARLY

  @NotNull(message = "A conta é obrigatória")
  private Long accountId;

  @NotNull(message = "A categoria é obrigatória")
  private Long categoryId;
}