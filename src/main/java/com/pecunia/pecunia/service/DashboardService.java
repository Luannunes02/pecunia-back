package com.pecunia.pecunia.service;

import com.pecunia.pecunia.dto.response.DashboardResponse;
import com.pecunia.pecunia.entity.Account;
import com.pecunia.pecunia.entity.Category;
import com.pecunia.pecunia.entity.Transaction;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.AccountRepository;
import com.pecunia.pecunia.repository.CategoryRepository;
import com.pecunia.pecunia.repository.TransactionRepository;
import com.pecunia.pecunia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

        private final TransactionRepository transactionRepository;
        private final AccountRepository accountRepository;
        private final UserRepository userRepository;

        public DashboardResponse getDashboardData(String userEmail) {
                User user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                LocalDateTime startDate = YearMonth.now().atDay(1).atStartOfDay();
                LocalDateTime endDate = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);

                List<Transaction> transactions = transactionRepository
                                .findByAccount_UserAndTransactionDateBetweenOrderByTransactionDateDescWithCategory(user,
                                                startDate, endDate);

                DashboardResponse response = new DashboardResponse();

                // Cálculo corrigido de receita, despesa e saldo do mês
                BigDecimal totalIncome = transactions.stream()
                                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()) && t.getAmount() != null
                                                && t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpense = transactions.stream()
                                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()) && t.getAmount() != null
                                                && t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                response.setTotalIncome(totalIncome);
                response.setTotalExpense(totalExpense);
                response.setBalance(totalIncome.subtract(totalExpense));

                // Resumo por categoria (mantém o agrupamento para exibir as categorias e
                // valores)
                List<DashboardResponse.CategorySummary> categorySummaries = transactions.stream()
                                .collect(Collectors.groupingBy(
                                                Transaction::getCategory,
                                                Collectors.mapping(
                                                                Transaction::getAmount,
                                                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                                .entrySet()
                                .stream()
                                .map(entry -> {
                                        DashboardResponse.CategorySummary summary = new DashboardResponse.CategorySummary();
                                        Category category = entry.getKey();
                                        summary.setCategoryName(category.getName());
                                        summary.setCategoryType(category.getType());
                                        summary.setTotal(entry.getValue());
                                        summary.setColorHex(category.getColorHex());
                                        return summary;
                                })
                                .collect(Collectors.toList());

                response.setCategorySummaries(categorySummaries);

                // Resumo mensal dos últimos 6 meses (mantém igual)
                List<DashboardResponse.MonthlySummary> monthlySummaries = new ArrayList<>();
                for (int i = 5; i >= 0; i--) {
                        YearMonth month = YearMonth.now().minusMonths(i);
                        LocalDateTime monthStart = month.atDay(1).atStartOfDay();
                        LocalDateTime monthEnd = month.atEndOfMonth().atTime(23, 59, 59);

                        List<Transaction> monthTransactions = transactions.stream()
                                        .filter(t -> !t.getTransactionDate().isBefore(monthStart)
                                                        && !t.getTransactionDate().isAfter(monthEnd))
                                        .collect(Collectors.toList());

                        Map<String, BigDecimal> monthTotals = monthTransactions.stream()
                                        .collect(Collectors.groupingBy(
                                                        Transaction::getType,
                                                        Collectors.mapping(
                                                                        Transaction::getAmount,
                                                                        Collectors.reducing(BigDecimal.ZERO,
                                                                                        BigDecimal::add))));

                        DashboardResponse.MonthlySummary summary = new DashboardResponse.MonthlySummary();
                        summary.setMonth(month.format(java.time.format.DateTimeFormatter.ofPattern("MM/yyyy")));
                        summary.setIncome(monthTotals.getOrDefault("INCOME", BigDecimal.ZERO));
                        summary.setExpense(monthTotals.getOrDefault("EXPENSE", BigDecimal.ZERO));
                        summary.setBalance(summary.getIncome().subtract(summary.getExpense()));
                        monthlySummaries.add(summary);
                }

                response.setMonthlySummaries(monthlySummaries);

                // Saldos das contas (mantém igual)
                Map<String, BigDecimal> accountBalances = accountRepository.findByUser(user).stream()
                                .collect(Collectors.toMap(
                                                Account::getName,
                                                Account::getBalance));

                response.setAccountBalances(accountBalances);

                return response;
        }
}