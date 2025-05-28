package com.pecunia.pecunia.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para registro de novo usuário")
public class UserRegistrationRequest {

  @NotBlank(message = "O nome é obrigatório")
  @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
  @Schema(description = "Nome completo do usuário", example = "João Silva")
  private String name;

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Email inválido")
  @Schema(description = "Email do usuário", example = "joao.silva@email.com")
  private String email;

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
  @Schema(description = "Senha do usuário", example = "senha123")
  private String password;
}