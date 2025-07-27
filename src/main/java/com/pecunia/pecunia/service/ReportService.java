package com.pecunia.pecunia.service;

import com.pecunia.pecunia.dto.request.ReportRequest;
import com.pecunia.pecunia.entity.Report;
import com.pecunia.pecunia.entity.Transaction;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.ReportRepository;
import com.pecunia.pecunia.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final TransactionRepository transactionRepository;
  private final UserService userService;
  private final ReportRepository reportRepository;

  public byte[] generateReport(ReportRequest request, String token) {
    User user = userService.getUserFromToken(token);
    LocalDateTime startDate = request.getStartDate();
    LocalDateTime endDate = request.getEndDate();
    String format = request.getFormat();

    List<Transaction> transactions = transactionRepository
        .findByAccount_UserAndTransactionDateBetweenOrderByTransactionDateDesc(
            user, startDate, endDate);

    if ("EXCEL".equalsIgnoreCase(format)) {
      return generateExcelReport(transactions, startDate, endDate);
    } else {
      throw new IllegalArgumentException("Formato de relatório não suportado: " + format);
    }
  }

  private byte[] generateExcelReport(List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate) {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Relatório de Transações");

      // Estilo para cabeçalho
      CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerStyle.setFont(headerFont);

      // Criar cabeçalho
      Row headerRow = sheet.createRow(0);
      String[] columns = { "Data", "Descrição", "Tipo", "Categoria", "Valor", "Conta" };
      for (int i = 0; i < columns.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(columns[i]);
        cell.setCellStyle(headerStyle);
      }

      // Preencher dados
      int rowNum = 1;
      for (Transaction transaction : transactions) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0)
            .setCellValue(transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        row.createCell(1).setCellValue(transaction.getDescription());
        row.createCell(2).setCellValue(transaction.getType());
        row.createCell(3).setCellValue(transaction.getCategory().getName());
        row.createCell(4).setCellValue(transaction.getAmount().doubleValue());
        row.createCell(5).setCellValue(transaction.getAccount().getName());
      }

      // Ajustar largura das colunas
      for (int i = 0; i < columns.length; i++) {
        sheet.autoSizeColumn(i);
      }

      // Adicionar totais
      Row totalRow = sheet.createRow(rowNum + 1);
      totalRow.createCell(0).setCellValue("TOTAIS");
      totalRow.createCell(0).setCellStyle(headerStyle);

      Map<String, Double> totalsByType = transactions.stream()
          .collect(Collectors.groupingBy(
              Transaction::getType,
              Collectors.summingDouble(t -> t.getAmount().doubleValue())));

      int colNum = 1;
      for (Map.Entry<String, Double> entry : totalsByType.entrySet()) {
        Cell cell = totalRow.createCell(colNum++);
        cell.setCellValue(entry.getValue());
        cell.setCellStyle(headerStyle);
      }

      // Converter para byte array
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);
      return outputStream.toByteArray();

    } catch (IOException e) {
      throw new RuntimeException("Erro ao gerar relatório Excel", e);
    }
  }

  @Transactional(readOnly = true)
  public List<Report> findAll() {
    return reportRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Report findById(Long id) {
    return reportRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Report> findByUser(Long userId) {
    User user = userService.findById(userId);
    return reportRepository.findByUser(user);
  }

  @Transactional
  public Report save(Report report) {
    report.setCreatedAt(LocalDateTime.now());
    report.setUpdatedAt(LocalDateTime.now());
    return reportRepository.save(report);
  }

  @Transactional
  public void delete(Long id) {
    reportRepository.deleteById(id);
  }
}