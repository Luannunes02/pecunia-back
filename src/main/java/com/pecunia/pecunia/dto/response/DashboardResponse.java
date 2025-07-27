package com.pecunia.pecunia.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
  private BigDecimal totalIncome;
  private BigDecimal totalExpense;
  private BigDecimal balance;
  private List<CategorySummary> categorySummaries;
  private List<MonthlySummary> monthlySummaries;
  private Map<String, BigDecimal> accountBalances;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CategorySummary {
    private String categoryName;
    private String categoryType;
    private BigDecimal total;
    private String colorHex;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MonthlySummary {
    private String month;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal balance;
  }
}