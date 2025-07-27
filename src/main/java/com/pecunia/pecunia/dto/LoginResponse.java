package com.pecunia.pecunia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
  private String token;
  private String type;
  private Long userId;
  private String name;
  private String email;
}