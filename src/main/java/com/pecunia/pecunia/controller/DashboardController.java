package com.pecunia.pecunia.controller;

import com.pecunia.pecunia.dto.response.DashboardResponse;
import com.pecunia.pecunia.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para visualização do dashboard")
public class DashboardController {

  private final DashboardService dashboardService;

  @GetMapping
  @Operation(summary = "Obter dados do dashboard")
  public ResponseEntity<DashboardResponse> getDashboardData(Authentication authentication) {
    String userEmail = authentication.getName();

    DashboardResponse data = dashboardService.getDashboardData(userEmail);

    return ResponseEntity.ok(data);
  }
}
