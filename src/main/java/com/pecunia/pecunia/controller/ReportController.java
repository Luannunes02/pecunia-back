package com.pecunia.pecunia.controller;

import com.pecunia.pecunia.dto.request.ReportRequest;
import com.pecunia.pecunia.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios")
public class ReportController {

  private final ReportService reportService;

  @PostMapping("/generate")
  @Operation(summary = "Gerar relatório financeiro")
  public ResponseEntity<byte[]> generateReport(
      @Valid @RequestBody ReportRequest request,
      Authentication authentication) {
    byte[] report = reportService.generateReport(request, authentication.getName());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "relatorio-financeiro.pdf");

    return ResponseEntity.ok()
        .headers(headers)
        .body(report);
  }
}