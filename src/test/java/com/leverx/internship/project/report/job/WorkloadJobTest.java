package com.leverx.internship.project.report.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.leverx.internship.project.department.repository.DepartmentRepository;
import com.leverx.internship.project.department.repository.entity.Department;
import com.leverx.internship.project.department.web.converter.DepartmentConverter;
import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.project.repository.ProjectRepository;
import com.leverx.internship.project.project.repository.entity.Project;
import com.leverx.internship.project.project.web.converter.ProjectConverter;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.report.excel.generator.ExcelWorkbookGenerator;
import com.leverx.internship.project.report.model.ReportType;
import com.leverx.internship.project.security.model.Role;
import com.leverx.internship.project.user.repository.entity.User;
import com.leverx.internship.project.user.web.converter.UserConverter;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkloadJobTest {

  @Mock
  private ProjectRepository projectRepository;
  @Mock
  private UserConverter userConverter;
  @Mock
  private DepartmentRepository departmentRepository;
  @Mock
  private ProjectConverter projectConverter;
  @Mock
  private DepartmentConverter departmentConverter;
  @Mock
  private ExcelWorkbookGenerator excelWorkbookGenerator;
  @Mock
  private Clock clock;
  private WorkloadJob workloadJobUnderTest;

  @BeforeEach
  void setUp() {
    when(clock.instant()).thenReturn(Instant.parse("2022-02-16T00:00:00.00Z"));
    workloadJobUnderTest = new WorkloadJob(projectRepository, userConverter,
        departmentRepository, projectConverter, departmentConverter,
        excelWorkbookGenerator, clock);
  }

  @Test
  void testCreateWorkloadReport_ShouldCloseWorkbook() throws Exception {
    when(clock.instant()).thenReturn(Instant.parse("2022-02-16T00:00:00.00Z"));
    final Workbook mockWorkbook = mock(Workbook.class);
    when(excelWorkbookGenerator.generateReportWorkbook(ReportType.WorkloadReport, Map.ofEntries(
        Map.entry(new DepartmentResponse(1, "name", "description"), Map.ofEntries(
            Map.entry(new UserResponse(1, "email", "password", "firstName", "lastName", Role.ADMIN,
                true), Set.of(
                new ProjectResponse(1, "name", "description", clock.instant(),
                    clock.instant())))))))).thenReturn(mockWorkbook);

    final List<Department> departments = List.of(
        new Department(1, "name", "description", clock.instant(), "createdBy",
            clock.instant(), "updatedBy", List.of(
            new User(1, "firstName", "lastName", "email", "password", true, Role.ADMIN,
                clock.instant(), clock.instant(), Set.of(
                new Project(1, "name", "description", clock.instant(),
                    clock.instant(), clock.instant(), clock.instant(),
                    "createdBy", "updatedBy", Set.of())), null)), Set.of(
            new Project(1, "name", "description", clock.instant(),
                clock.instant(), clock.instant(), clock.instant(),
                "createdBy", "updatedBy", Set.of(
                new User(1, "firstName", "lastName", "email", "password", true, Role.ADMIN,
                    clock.instant(), clock.instant(), Set.of(), null))))));
    when(departmentRepository.findAll()).thenReturn(departments);

    final UserResponse userResponse = new UserResponse(1, "email", "password", "firstName",
        "lastName", Role.ADMIN, true);
    when(userConverter.toUserResponse(any(User.class))).thenReturn(userResponse);

    final ProjectResponse projectResponse = new ProjectResponse(1, "name", "description",
        clock.instant(), clock.instant());
    final Set<ProjectResponse> projectResponses = Set.of(projectResponse);

    final Collection<Project> projects = List.of(
        new Project(1, "name", "description", clock.instant(), clock.instant(),
            clock.instant(), clock.instant(), "createdBy", "updatedBy", Set.of(
            new User(1, "firstName", "lastName", "email", "password", true, Role.ADMIN,
                clock.instant(), clock.instant(), Set.of(),
                departments.get(0)))));
    when(projectRepository.findActiveProjectForCurrentMonthByEmployeeId(1,
        clock.instant().minus(30, ChronoUnit.DAYS),
        clock.instant())).thenReturn(projects);
    when(projectConverter.projectSetToProjectRespSet(new HashSet<>(projects))).thenReturn(
        projectResponses);

    final DepartmentResponse departmentResponse = new DepartmentResponse(1, "name", "description");
    when(departmentConverter.toDepartmentResponse(any(Department.class))).thenReturn(
        departmentResponse);

    workloadJobUnderTest.createWorkloadReport();

    verify(mockWorkbook).close();
  }

  @Test
  void testCollectWorkloadData_ShouldReturnMap() {
    when(clock.instant()).thenReturn(Instant.parse("2022-02-16T00:00:00.00Z"));
    final ProjectResponse projectResponse = new ProjectResponse(1, "name", "description",
        clock.instant(), clock.instant());
    final Map<DepartmentResponse, Map<UserResponse, Set<ProjectResponse>>> expectedResult = Map.ofEntries(
        Map.entry(new DepartmentResponse(1, "name", "description"), Map.ofEntries(
            Map.entry(new UserResponse(1, "email", "password", "firstName", "lastName", Role.ADMIN,
                true), Set.of(projectResponse)))));

    final List<Department> departments = List.of(
        new Department(1, "name", "description", clock.instant(), "createdBy",
            clock.instant(), "updatedBy", List.of(
            new User(1, "firstName", "lastName", "email", "password", true, Role.ADMIN,
                clock.instant(), clock.instant(), Set.of(
                new Project(1, "name", "description", clock.instant(),
                    clock.instant(), clock.instant(), clock.instant(),
                    "createdBy", "updatedBy", Set.of())), null)), Set.of(
            new Project(1, "name", "description", clock.instant(),
                clock.instant(), clock.instant(), clock.instant(),
                "createdBy", "updatedBy", Set.of(
                new User(1, "firstName", "lastName", "email", "password", true, Role.ADMIN,
                    clock.instant(), clock.instant(), Set.of(), null))))));
    when(departmentRepository.findAll()).thenReturn(departments);

    final UserResponse userResponse = new UserResponse(1, "email", "password", "firstName",
        "lastName", Role.ADMIN, true);
    when(userConverter.toUserResponse(any(User.class))).thenReturn(userResponse);

    final ProjectResponse projectResponse1 = new ProjectResponse();
    projectResponse1.setId(1);
    projectResponse1.setName("name");
    projectResponse1.setDescription("description");
    projectResponse1.setDateStart(clock.instant());
    projectResponse1.setDateEnd(clock.instant());
    final Set<ProjectResponse> projectResponses = Set.of(projectResponse1);

    final Collection<Project> projects = List.of(
        new Project(1, "name", "description", clock.instant().minus(30, ChronoUnit.DAYS),
            clock.instant(),
            clock.instant(), clock.instant(), "createdBy", "updatedBy", Set.of(
            new User(1, "firstName", "lastName", "email", "password", true, Role.ADMIN,
                clock.instant(), clock.instant(), Set.of(),
                new Department(1, "name", "description", clock.instant(), "createdBy",
                    clock.instant(), "updatedBy", List.of(), Set.of())))));
    when(projectRepository.findActiveProjectForCurrentMonthByEmployeeId(1,
        clock.instant().minus(30, ChronoUnit.DAYS),
        clock.instant())).thenReturn(projects);

    when(projectConverter.projectSetToProjectRespSet(new HashSet<>(projects))).thenReturn(
        projectResponses);
    final DepartmentResponse departmentResponse = new DepartmentResponse(1, "name", "description");
    when(departmentConverter.toDepartmentResponse(any(Department.class))).thenReturn(
        departmentResponse);

    final Map<DepartmentResponse, Map<UserResponse, Set<ProjectResponse>>> result = workloadJobUnderTest.collectWorkloadData();

    assertEquals(expectedResult, result);
  }
}