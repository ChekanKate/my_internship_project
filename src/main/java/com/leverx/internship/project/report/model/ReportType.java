package com.leverx.internship.project.report.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportType {
  WorkloadReport(new String[] {"Department", "Employee full name", "Project", "Date start",
      "Date end"},
      "Workload report"),
  AvailableEmployeeReport(new String[] {"Department", "Employee full name", "Project", "Date end"},
      "Available employee report");

  private String[] header;
  private String reportName;

  public String[] getHeader() {
    return header;
  }

  public void setHeader(String[] header) {
    this.header = header;
  }

  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }
}
