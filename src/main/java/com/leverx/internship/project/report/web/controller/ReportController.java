package com.leverx.internship.project.report.web.controller;

import com.leverx.internship.project.exception.ReportNotFoundException;
import com.leverx.internship.project.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@PropertySource("classpath:application.properties")
public class ReportController {
  private final ReportService reportService;
  @Value("${report.content.type}")
  public String excelXlsxContentType;
  @Value("${report.header.attachment}")
  private String headerAttachment;

  @Autowired
  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/workload")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Downloading workload report")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public void downloadWorkloadReport(
      @Parameter(description = "Required month", name = "month")
          @RequestParam(value = "month", required = false)
          String month,
      HttpServletResponse response) {
    response.setContentType(excelXlsxContentType);
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerAttachment);
    Workbook workbook = reportService.downloadWorkloadReport(month);
    writeFile(workbook, response);
  }

  @GetMapping("/available")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Downloading workload report")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public void downloadAvailableEmployeeReport(
      @Parameter(description = "Required month", name = "month")
          @RequestParam(value = "month", required = false)
          String month,
      HttpServletResponse response) {
    response.setContentType(excelXlsxContentType);
    response.addHeader(HttpHeaders.CONTENT_DISPOSITION, headerAttachment);
    Workbook workbook = reportService.downloadAvailableEmployeeReport(month);
    writeFile(workbook, response);
  }

  private void writeFile(Workbook workbook, HttpServletResponse response) {
    try (ServletOutputStream outputStream = response.getOutputStream()) {
      workbook.write(outputStream);
      workbook.close();
    } catch (IOException e) {
      throw new ReportNotFoundException(e.getMessage());
    }
  }
}