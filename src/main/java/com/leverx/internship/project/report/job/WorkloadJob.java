package com.leverx.internship.project.report.job;

import com.leverx.internship.project.department.repository.DepartmentRepository;
import com.leverx.internship.project.department.repository.entity.Department;
import com.leverx.internship.project.department.web.converter.DepartmentConverter;
import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.exception.ReportNotFoundException;
import com.leverx.internship.project.project.repository.ProjectRepository;
import com.leverx.internship.project.project.web.converter.ProjectConverter;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.report.excel.generator.ExcelWorkbookGenerator;
import com.leverx.internship.project.report.model.ReportType;
import com.leverx.internship.project.user.web.converter.UserConverter;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@PropertySource("classpath:application.properties")
public class WorkloadJob {

  private final ProjectRepository projectRepository;
  private final UserConverter userConverter;
  private final DepartmentRepository departmentRepository;
  private final ProjectConverter projectConverter;
  private final DepartmentConverter departmentConverter;
  private final ExcelWorkbookGenerator excelWorkbookGenerator;
  private final ReportType reportType = ReportType.WorkloadReport;
  private static final String FILE_NAME = "%sWorkload_by_department-%s.xlsx";
  @Value("${output.file.path}")
  private String pathForOutput;
  private final Instant dateMonthAgo;
  private final Instant dateCurrentMonth;

  @Autowired
  public WorkloadJob(
      ProjectRepository projectRepository,
      UserConverter userConverter,
      DepartmentRepository departmentRepository,
      ProjectConverter projectConverter,
      DepartmentConverter departmentConverter,
      ExcelWorkbookGenerator excelWorkbookGenerator,
      Clock clock) {
    this.projectRepository = projectRepository;
    this.userConverter = userConverter;
    this.departmentRepository = departmentRepository;
    this.projectConverter = projectConverter;
    this.departmentConverter = departmentConverter;
    this.excelWorkbookGenerator = excelWorkbookGenerator;
    this.dateMonthAgo = clock.instant().minus(30, ChronoUnit.DAYS);
    this.dateCurrentMonth = clock.instant();
  }

  @Transactional(readOnly = true)
  @Scheduled(cron = "${cron.expression}")
  public void createWorkloadReport() {
    File file =
        new File(String.format(FILE_NAME, pathForOutput, LocalDate.now().getMonth().name()));
    try (FileOutputStream fos = new FileOutputStream(file)) {
      Workbook workbook = excelWorkbookGenerator.generateReportWorkbook(reportType,
          collectWorkloadData());
      workbook.write(fos);
      workbook.close();
    } catch (IOException e) {
      throw new ReportNotFoundException(e.getMessage());
    }
  }

  @Transactional(readOnly = true)
  public Map<DepartmentResponse, Map<UserResponse, Set<ProjectResponse>>> collectWorkloadData() {
    List<Department> departments = departmentRepository.findAll();
    Map<DepartmentResponse, Map<UserResponse, Set<ProjectResponse>>> departmentsWorkload =
        new HashMap<>();
    departments.forEach(
        department -> {
          Map<UserResponse, Set<ProjectResponse>> hashMap = new HashMap<>();
          department.getEmployees().forEach(
              employee ->
                  hashMap.put(
                      userConverter.toUserResponse(employee),
                      projectConverter.projectSetToProjectRespSet(new HashSet<>(projectRepository
                          .findActiveProjectForCurrentMonthByEmployeeId(employee.getId(),
                              dateMonthAgo, dateCurrentMonth)))));
          departmentsWorkload.put(departmentConverter.toDepartmentResponse(department), hashMap);
        });

    return new TreeMap<>(departmentsWorkload);
  }
}
