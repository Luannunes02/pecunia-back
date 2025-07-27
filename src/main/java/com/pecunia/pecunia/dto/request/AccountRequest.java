package com.pecunia.pecunia.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {
  @NotBlank(message = "O nome é obrigatório")
  private String name;

  @NotBlank(message = "O tipo é obrigatório")
  private String type; // CHECKING, SAVINGS, CREDIT_CARD, INVESTMENT

  @NotNull(message = "O saldo inicial é obrigatório")
  @Positive(message = "O saldo inicial deve ser positivo")
  private BigDecimal initialBalance;
}