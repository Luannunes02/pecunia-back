package com.pecunia.pecunia.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO para resposta de autenticação")
public class AuthResponse {

  @Schema(description = "Token JWT de autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  private String token;

  @Schema(description = "Tipo do token", example = "Bearer")
  private String type;

  @Schema(description = "ID do usuário", example = "1")
  private Long userId;

  @Schema(description = "Nome do usuário", example = "João Silva")
  private String name;

  @Schema(description = "Email do usuário", example = "joao.silva@email.com")
  private String email;
}