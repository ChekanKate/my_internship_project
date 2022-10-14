package com.leverx.internship.project.report.service;

import org.apache.poi.ss.usermodel.Workbook;

public interface ReportService {

  Workbook downloadWorkloadReport(String month);

  Workbook downloadAvailableEmployeeReport(String month);
}
