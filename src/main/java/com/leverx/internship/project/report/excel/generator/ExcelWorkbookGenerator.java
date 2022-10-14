package com.leverx.internship.project.report.excel.generator;

import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.report.model.ReportType;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExcelWorkbookGenerator {

  public Workbook generateReportWorkbook(ReportType reportType,
      Map<DepartmentResponse,
          Map<UserResponse, Set<ProjectResponse>>> reportResult) {
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet spreadsheet = workbook.createSheet(reportType.getReportName());
    XSSFRow row;
    Map<String, Object[]> reportData = new TreeMap<>();
    generateDataForRows(reportData, reportResult, reportType);
    Set<String> keyId = reportData.keySet();
    int rowId = 0;

    for (String key : keyId) {
      row = spreadsheet.createRow(rowId++);
      Object[] objectArr = reportData.get(key);
      int cellId = 0;

      for (Object obj : objectArr) {
        Cell cell = row.createCell(cellId++);
        cell.setCellValue(obj.toString());
      }
    }
    return workbook;
  }

  private void generateDataForRows(
      Map<String, Object[]> reportData,
      Map<DepartmentResponse, Map<UserResponse, Set<ProjectResponse>>> workloadJobResult,
      ReportType reportType) {
    int initialRowValue = 1;
    AtomicInteger numberOfRow = new AtomicInteger(initialRowValue);
    reportData.put(String.valueOf(numberOfRow.get()), reportType.getHeader());
    numberOfRow.getAndIncrement();

    if (ReportType.WorkloadReport.equals(reportType)) {
      workloadJobResult.forEach(
          (departmentData, map) ->
              workloadJobResult
                  .get(departmentData)
                  .forEach(
                      (employee, set) ->
                          set.forEach(
                              project -> {
                                reportData.put(
                                    String.valueOf(numberOfRow.get()),
                                    new Object[]{
                                        departmentData.getName(),
                                        employee.getFirstName() + " " + employee.getLastName(),
                                        project.getName(),
                                        project.getDateStart(),
                                        project.getDateEnd()
                                    });
                                numberOfRow.getAndIncrement();
                              })));
    } else if (ReportType.AvailableEmployeeReport.equals(reportType)) {
      workloadJobResult.forEach(
          (departmentData, map) ->
              workloadJobResult
                  .get(departmentData)
                  .forEach(
                      (employee, set) ->
                          set.forEach(
                              project -> {
                                reportData.put(
                                    String.valueOf(numberOfRow.get()),
                                    new Object[]{
                                        departmentData.getName(),
                                        employee.getFirstName() + " " + employee.getLastName(),
                                        project.getName(),
                                        project.getDateEnd()
                                    });
                                numberOfRow.getAndIncrement();
                              })));
    }
  }
}