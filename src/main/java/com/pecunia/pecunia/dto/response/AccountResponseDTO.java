package com.pecunia.pecunia.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponseDTO {
  private Long id;
  private String name;
  private String type;
  private BigDecimal balance;
}
