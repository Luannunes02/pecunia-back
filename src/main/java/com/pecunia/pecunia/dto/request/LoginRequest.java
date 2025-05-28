package com.pecunia.pecunia.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para autenticação de usuário")
public class LoginRequest {

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Email inválido")
  @Schema(description = "Email do usuário", example = "joao.silva@email.com")
  private String email;

  @NotBlank(message = "A senha é obrigatória")
  @Schema(description = "Senha do usuário", example = "senha123")
  private String password;
}