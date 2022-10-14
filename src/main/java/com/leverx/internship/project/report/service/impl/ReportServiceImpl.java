package com.leverx.internship.project.report.service.impl;

import com.leverx.internship.project.exception.ReportNotFoundException;
import com.leverx.internship.project.report.model.ReportType;
import com.leverx.internship.project.report.service.ReportService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl implements ReportService {

  @Value("${output.file.path}")
  private String reportDirectory;

  private static final String WORKLOAD_REPORT_FILE_NAME_PATTERN =
      "^Workload_by_department-%s.xlsx$";
  private static final String AVAILABLE_EMPLOYEE_REPORT_FILE_NAME_PATTERN =
      "^Available_employee-%s.xlsx$";

  @Override
  @Transactional(readOnly = true)
  public XSSFWorkbook downloadWorkloadReport(String monthString) {
    ReportType reportType = ReportType.WorkloadReport;
    try {
      return new XSSFWorkbook(findReportByMonth(monthString, reportType));
    } catch (IOException | InvalidFormatException e) {
      throw new ReportNotFoundException(e.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public XSSFWorkbook downloadAvailableEmployeeReport(String monthString) {
    ReportType reportType = ReportType.AvailableEmployeeReport;
    try {
      return new XSSFWorkbook(findReportByMonth(monthString, reportType));
    } catch (IOException | InvalidFormatException e) {
      throw new ReportNotFoundException(e.getMessage());
    }
  }

  private File findReportByMonth(String monthString, ReportType reportType) {
    Month month;
    if (monthString == null) {
      month = LocalDate.now().getMonth();
    } else {
      monthString = monthString.toUpperCase();
      month = Month.valueOf(monthString);
    }
    File[] files = new File(reportDirectory).listFiles();
    if (files == null) {
      throw new ReportNotFoundException("No reports in directory");
    }
    if (ReportType.WorkloadReport.equals(reportType)) {
      return Arrays.stream(files)
          .filter(file -> {
            String name = file.getName();
            return name.matches(
                String.format(WORKLOAD_REPORT_FILE_NAME_PATTERN, month.name().toUpperCase()));
          })
          .findFirst()
          .orElseThrow(() -> new ReportNotFoundException("Such file doesn't exist"));
    } else if (ReportType.AvailableEmployeeReport.equals(reportType)) {
      return Arrays.stream(files)
          .filter(
              file -> file.getName().matches(
                  String.format(AVAILABLE_EMPLOYEE_REPORT_FILE_NAME_PATTERN,
                      month.name().toUpperCase())))
          .findFirst()
          .orElseThrow(() -> new ReportNotFoundException("Such file doesn't exist"));
    } else {
      throw new ReportNotFoundException("Report with this type doesn't exist");
    }
  }
}
