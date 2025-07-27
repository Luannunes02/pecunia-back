package com.pecunia.pecunia.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
  @NotBlank(message = "O nome é obrigatório")
  private String name;

  @NotBlank(message = "O tipo é obrigatório")
  private String type; // INCOME, EXPENSE

  private String iconName;
  private String colorHex;
}