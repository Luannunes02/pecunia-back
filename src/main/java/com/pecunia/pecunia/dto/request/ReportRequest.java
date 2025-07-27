package com.pecunia.pecunia.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String format; // PDF, EXCEL, etc.
}