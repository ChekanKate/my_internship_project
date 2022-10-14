package com.leverx.internship.project.department.service.impl;

import com.leverx.internship.project.department.repository.DepartmentRepository;
import com.leverx.internship.project.department.repository.entity.Department;
import com.leverx.internship.project.department.web.converter.DepartmentConverter;
import com.leverx.internship.project.department.web.dto.request.DepartmentBodyRequest;
import com.leverx.internship.project.department.web.dto.request.DepartmentParamRequest;
import com.leverx.internship.project.department.web.dto.response.DepartmentProjectsResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentUsersResponse;
import com.leverx.internship.project.project.repository.entity.Project;
import com.leverx.internship.project.project.service.ProjectService;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.security.model.JwtUser;
import com.leverx.internship.project.security.model.Role;
import com.leverx.internship.project.user.repository.entity.User;
import com.leverx.internship.project.user.service.UserService;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.HashSet;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

  @Mock private DepartmentRepository departmentRepository;
  @Mock private DepartmentConverter departmentConverter;
  @Mock private UserService userService;
  @Mock private ProjectService projectService;
  private DepartmentServiceImpl departmentServiceImplUnderTest;
  @Mock private Clock clock;

  @BeforeEach
  public void init() {
    departmentServiceImplUnderTest =
        new DepartmentServiceImpl(
            departmentRepository, departmentConverter, userService, projectService);
  }

  @Test
  void testFindAll_ShouldReturnList() {
    final DepartmentParamRequest departmentParamRequest =
        new DepartmentParamRequest("name", "description");
    final DepartmentResponse departmentResponse = new DepartmentResponse();
    departmentResponse.setId(1);
    departmentResponse.setName("name");
    departmentResponse.setDescription("description");
    final List<DepartmentResponse> expectedResult = List.of(departmentResponse);

    final User user = new User();
    user.setId(1);
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setEmail("email");
    user.setPassword("password");
    user.setActive(false);
    user.setRole(Role.ADMIN);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    user.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final User user1 = new User();
    user1.setId(1);
    user1.setFirstName("firstName");
    user1.setLastName("lastName");
    user1.setEmail("email");
    user1.setPassword("password");
    user1.setActive(false);
    user1.setRole(Role.ADMIN);
    user1.setCreatedAt(clock.instant());
    user1.setUpdatedAt(clock.instant());
    user1.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final Page<Department> departments =
        new PageImpl<>(
            List.of(
                new Department(
                    1,
                    "name",
                    "description",
                    clock.instant(),
                    "mail",
                    clock.instant(),
                    "mail",
                    List.of(user),
                    Set.of(
                        new Project(
                            1,
                            "name",
                            "description",
                            clock.instant(),
                            clock.instant(),
                            clock.instant(),
                            clock.instant(),
                            "mail",
                            "mail",
                            Set.of(user1))))));
    when(departmentRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(departments);

    final DepartmentResponse departmentResponse1 = new DepartmentResponse();
    departmentResponse1.setId(1);
    departmentResponse1.setName("name");
    departmentResponse1.setDescription("description");
    when(departmentConverter.toDepartmentResponse(any(Department.class)))
        .thenReturn(departmentResponse1);

    final List<DepartmentResponse> result =
        departmentServiceImplUnderTest.findAll(0, 3, departmentParamRequest);

    assertEquals(expectedResult, result);
  }

  @Test
  void testFindById_ShouldReturnDepartment() {

    final DepartmentResponse expectedResult = new DepartmentResponse(1, "name", "description");

    final Optional<Department> department = Optional.of(
        new Department(1, "name", "description", clock.instant(), "createdBy",
            clock.instant(), "updatedBy", List.of(
            new User(1, "firstName", "lastName", "email", "password", false, Role.ADMIN,
                clock.instant(), clock.instant(), Set.of(
                new Project(1, "name", "description", clock.instant(),
                    clock.instant(), clock.instant(), clock.instant(),
                    "createdBy", "updatedBy", Set.of())), null)), Set.of(
            new Project(1, "name", "description", clock.instant(),
                clock.instant(), clock.instant(), clock.instant(),
                "createdBy", "updatedBy", Set.of(
                new User(1, "firstName", "lastName", "email", "password", false, Role.ADMIN,
                    clock.instant(), clock.instant(), Set.of(), null))))));
    when(departmentRepository.findById(1)).thenReturn(department);

    final UserResponse userResponse = new UserResponse(1, "email", "password", "firstName",
        "lastName", Role.ADMIN, false);
    when(userService.findUserByEmail("email")).thenReturn(userResponse);

    when(userService.getCurrentUser()).thenReturn(new JwtUser(1, "email", "alex", "alex", "1234", true, Role.ADMIN.getAuthorities()));

    final DepartmentUsersResponse departmentUsersResponse = new DepartmentUsersResponse(1, "name",
        List.of(
            new UserResponse(1, "email", "password", "firstName", "lastName", Role.ADMIN, false)));
    when(departmentConverter.toDepartmentsUsersResponse(any(Department.class))).thenReturn(
        departmentUsersResponse);

    final DepartmentResponse departmentResponse = new DepartmentResponse(1, "name", "description");
    when(departmentConverter.toDepartmentResponse(any(Department.class))).thenReturn(
        departmentResponse);

    final DepartmentResponse result = departmentServiceImplUnderTest.findById(1);

    assertEquals(expectedResult, result);
  }

  @Test
  void testCreate_ShouldCreate() {
    final DepartmentBodyRequest departmentBodyRequest =
        new DepartmentBodyRequest("name", "description");
    final DepartmentResponse expectedResult = new DepartmentResponse();
    expectedResult.setId(1);
    expectedResult.setName("name");
    expectedResult.setDescription("description");

    final User user = new User();
    user.setId(1);
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setEmail("email");
    user.setPassword("password");
    user.setActive(false);
    user.setRole(Role.ADMIN);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    user.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final User user1 = new User();
    user1.setId(1);
    user1.setFirstName("firstName");
    user1.setLastName("lastName");
    user1.setEmail("email");
    user1.setPassword("password");
    user1.setActive(false);
    user1.setRole(Role.ADMIN);
    user1.setCreatedAt(clock.instant());
    user1.setUpdatedAt(clock.instant());
    user1.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final Department department =
        new Department(
            1,
            "name",
            "description",
            clock.instant(),
            "mail",
            clock.instant(),
            "mail",
            List.of(user),
            Set.of(
                new Project(
                    1,
                    "name",
                    "description",
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    "mail",
                    "mail",
                    Set.of(user1))));
    when(departmentConverter.toEntity(new DepartmentBodyRequest("name", "description")))
        .thenReturn(department);

    final DepartmentResponse departmentResponse = new DepartmentResponse();
    departmentResponse.setId(1);
    departmentResponse.setName("name");
    departmentResponse.setDescription("description");
    when(departmentConverter.toDepartmentResponse(any(Department.class)))
        .thenReturn(departmentResponse);

    final User user2 = new User();
    user2.setId(1);
    user2.setFirstName("firstName");
    user2.setLastName("lastName");
    user2.setEmail("email");
    user2.setPassword("password");
    user2.setActive(false);
    user2.setRole(Role.ADMIN);
    user2.setCreatedAt(clock.instant());
    user2.setUpdatedAt(clock.instant());
    user2.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final User user3 = new User();
    user3.setId(1);
    user3.setFirstName("firstName");
    user3.setLastName("lastName");
    user3.setEmail("email");
    user3.setPassword("password");
    user3.setActive(false);
    user3.setRole(Role.ADMIN);
    user3.setCreatedAt(clock.instant());
    user3.setUpdatedAt(clock.instant());
    user3.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final Department department1 =
        new Department(
            1,
            "name",
            "description",
            clock.instant(),
            "mail",
            clock.instant(),
            "mail",
            List.of(user2),
            Set.of(
                new Project(
                    1,
                    "name",
                    "description",
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    "mail",
                    "mail",
                    Set.of(user3))));
    when(departmentRepository.save(any(Department.class))).thenReturn(department1);

    final DepartmentResponse result = departmentServiceImplUnderTest.create(departmentBodyRequest);

    assertEquals(expectedResult, result);
  }

  @Test
  void testUpdate_ShouldUpdate() {
    final DepartmentBodyRequest departmentBodyRequestToUpdate =
        new DepartmentBodyRequest("name", "description");
    final DepartmentResponse expectedResult = new DepartmentResponse();
    expectedResult.setId(1);
    expectedResult.setName("name");
    expectedResult.setDescription("description");

    final DepartmentResponse departmentResponse = new DepartmentResponse();
    departmentResponse.setId(1);
    departmentResponse.setName("name");
    departmentResponse.setDescription("description");
    when(departmentConverter.toUpdatedDepartmentDto(
        eq(new DepartmentBodyRequest("name", "description")), any(Department.class)))
        .thenReturn(departmentResponse);

    final User user = new User();
    user.setId(1);
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setEmail("email");
    user.setPassword("password");
    user.setActive(false);
    user.setRole(Role.ADMIN);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    user.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final User user1 = new User();
    user1.setId(1);
    user1.setFirstName("firstName");
    user1.setLastName("lastName");
    user1.setEmail("email");
    user1.setPassword("password");
    user1.setActive(false);
    user1.setRole(Role.ADMIN);
    user1.setCreatedAt(clock.instant());
    user1.setUpdatedAt(clock.instant());
    user1.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final Optional<Department> department =
        Optional.of(
            new Department(
                1,
                "name",
                "description",
                clock.instant(),
                "mail",
                clock.instant(),
                "mail",
                List.of(user),
                Set.of(
                    new Project(
                        1,
                        "name",
                        "description",
                        clock.instant(),
                        clock.instant(),
                        clock.instant(),
                        clock.instant(),
                        "mail",
                        "mail",
                        Set.of(user1)))));
    when(departmentRepository.findById(1)).thenReturn(department);

    final User user2 = new User();
    user2.setId(1);
    user2.setFirstName("firstName");
    user2.setLastName("lastName");
    user2.setEmail("email");
    user2.setPassword("password");
    user2.setActive(false);
    user2.setRole(Role.ADMIN);
    user2.setCreatedAt(clock.instant());
    user2.setUpdatedAt(clock.instant());
    user2.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final User user3 = new User();
    user3.setId(1);
    user3.setFirstName("firstName");
    user3.setLastName("lastName");
    user3.setEmail("email");
    user3.setPassword("password");
    user3.setActive(false);
    user3.setRole(Role.ADMIN);
    user3.setCreatedAt(clock.instant());
    user3.setUpdatedAt(clock.instant());
    user3.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final Department department1 =
        new Department(
            1,
            "name",
            "description",
            clock.instant(),
            "mail",
            clock.instant(),
            "mail",
            List.of(user2),
            Set.of(
                new Project(
                    1,
                    "name",
                    "description",
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    "mail",
                    "mail",
                    Set.of(user3))));
    when(departmentConverter.toEntity(departmentResponse)).thenReturn(department1);
    final DepartmentResponse departmentResponse1 = new DepartmentResponse();
    departmentResponse1.setId(1);
    departmentResponse1.setName("name");
    departmentResponse1.setDescription("description");
    when(departmentConverter.toDepartmentResponse(any(Department.class)))
        .thenReturn(departmentResponse1);

    final User user4 = new User();
    user4.setId(1);
    user4.setFirstName("firstName");
    user4.setLastName("lastName");
    user4.setEmail("email");
    user4.setPassword("password");
    user4.setActive(false);
    user4.setRole(Role.ADMIN);
    user4.setCreatedAt(clock.instant());
    user4.setUpdatedAt(clock.instant());
    user4.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final User user5 = new User();
    user5.setId(1);
    user5.setFirstName("firstName");
    user5.setLastName("lastName");
    user5.setEmail("email");
    user5.setPassword("password");
    user5.setActive(false);
    user5.setRole(Role.ADMIN);
    user5.setCreatedAt(clock.instant());
    user5.setUpdatedAt(clock.instant());
    user5.setProjects(
        Set.of(
            new Project(
                1,
                "name",
                "description",
                clock.instant(),
                clock.instant(),
                clock.instant(),
                clock.instant(),
                "mail",
                "mail",
                Set.of(new User()))));
    final Department department2 =
        new Department(
            1,
            "name",
            "description",
            clock.instant(),
            "mail",
            clock.instant(),
            "mail",
            List.of(user4),
            Set.of(
                new Project(
                    1,
                    "name",
                    "description",
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    clock.instant(),
                    "mail",
                    "mail",
                    Set.of(user5))));
    when(departmentRepository.save(any(Department.class))).thenReturn(department2);

    final DepartmentResponse result =
        departmentServiceImplUnderTest.update(1, departmentBodyRequestToUpdate);

    assertEquals(expectedResult, result);
  }

  @Test
  void testDelete_ShouldDelete() {

    final Optional<Department> department =
        Optional.of(
            new Department(
                1,
                "name",
                "description",
                clock.instant(),
                "mail",
                clock.instant(),
                "mail",
                new ArrayList<>(),
                new HashSet<>()));
    when(departmentRepository.findById(1)).thenReturn(department);

    departmentServiceImplUnderTest.delete(1);

    verify(departmentRepository).deleteById(1);
  }

  @Test
  void testAddProjectToDepartment_ShouldAddProject() {
    final ProjectResponse projectResponse = new ProjectResponse();
    projectResponse.setId(1);
    projectResponse.setName("name");
    projectResponse.setDescription("description");
    projectResponse.setDateStart(clock.instant());
    projectResponse.setDateEnd(clock.instant());
    final DepartmentProjectsResponse expectedResult =
        new DepartmentProjectsResponse(1, "name", Set.of(projectResponse));

    final ProjectResponse projectResponse1 = new ProjectResponse();
    projectResponse1.setId(1);
    projectResponse1.setName("name");
    projectResponse1.setDescription("description");
    projectResponse1.setDateStart(clock.instant());
    projectResponse1.setDateEnd(clock.instant());
    final DepartmentProjectsResponse departmentProjectsResponse =
        new DepartmentProjectsResponse(1, "name", new HashSet<>());
    when(departmentConverter.toDepartmentProjectsResponse(any(Department.class)))
        .thenReturn(departmentProjectsResponse);

    final Optional<Department> department =
        Optional.of(
            new Department(
                1,
                "name",
                "description",
                clock.instant(),
                "mail",
                clock.instant(),
                "mail",
                new ArrayList<>(),
                new HashSet<>()));
    when(departmentRepository.findById(1)).thenReturn(department);

    final ProjectResponse projectResponse2 = new ProjectResponse();
    projectResponse2.setId(1);
    projectResponse2.setName("name");
    projectResponse2.setDescription("description");
    projectResponse2.setDateStart(clock.instant());
    projectResponse2.setDateEnd(clock.instant());
    when(projectService.findById(1)).thenReturn(projectResponse2);

    when(departmentConverter.toUpdatedEntity(
        eq(new DepartmentProjectsResponse(1, "name", Set.of(projectResponse2))),
        any(Department.class)))
        .thenReturn(department.get());

    final Department department2 =
        new Department(
            1,
            "name",
            "description",
            clock.instant(),
            "mail",
            clock.instant(),
            "mail",
            new ArrayList<>(),
            new HashSet<>());
    when(departmentRepository.save(any(Department.class))).thenReturn(department2);

    final DepartmentProjectsResponse result =
        departmentServiceImplUnderTest.addProjectToDepartment(1, 1);

    assertEquals(expectedResult, result);
  }

  @Test
  void testRemoveProjectFromDepartment_ShouldRemoveProject() {
    final ProjectResponse projectResponse = new ProjectResponse();
    projectResponse.setId(1);
    projectResponse.setName("name");
    projectResponse.setDescription("description");
    projectResponse.setDateStart(clock.instant());
    projectResponse.setDateEnd(clock.instant());
    final DepartmentProjectsResponse departmentProjectsResponse =
        new DepartmentProjectsResponse(1, "name", new HashSet<>());
    departmentProjectsResponse.addProject(projectResponse);
    when(departmentConverter.toDepartmentProjectsResponse(any(Department.class)))
        .thenReturn(departmentProjectsResponse);

    final Optional<Department> department =
        Optional.of(
            new Department(
                1,
                "name",
                "description",
                clock.instant(),
                "mail",
                clock.instant(),
                "mail",
                new ArrayList<>(),
                new HashSet<>()));
    when(departmentRepository.findById(1)).thenReturn(department);

    final ProjectResponse projectResponse1 = new ProjectResponse();
    projectResponse1.setId(1);
    projectResponse1.setName("name");
    projectResponse1.setDescription("description");
    projectResponse1.setDateStart(clock.instant());
    projectResponse1.setDateEnd(clock.instant());
    when(projectService.findById(1)).thenReturn(projectResponse1);

    final Department department1 =
        new Department(
            1,
            "name",
            "description",
            clock.instant(),
            "mail",
            clock.instant(),
            "mail",
            new ArrayList<>(),
            new HashSet<>());
    when(departmentConverter.toUpdatedEntity(
        eq(new DepartmentProjectsResponse(1, "name", new HashSet<>())), any(Department.class)))
        .thenReturn(department1);

    when(departmentRepository.save(any(Department.class))).thenReturn(department1);

    departmentServiceImplUnderTest.removeProjectFromDepartment(1, 1);

    verify(departmentRepository).save(any(Department.class));
  }
}