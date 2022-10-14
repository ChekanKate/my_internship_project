package com.leverx.internship.project.project.service.impl;

import com.leverx.internship.project.department.repository.DepartmentRepository;
import com.leverx.internship.project.department.repository.entity.Department;
import com.leverx.internship.project.project.repository.ProjectRepository;
import com.leverx.internship.project.project.repository.entity.Project;
import com.leverx.internship.project.project.service.ProjectService;
import com.leverx.internship.project.project.web.converter.ProjectConverter;
import com.leverx.internship.project.project.web.dto.request.ProjectBodyRequest;
import com.leverx.internship.project.project.web.dto.request.ProjectParamRequest;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.project.web.dto.response.ProjectUsersResponse;
import com.leverx.internship.project.security.model.JwtUser;
import com.leverx.internship.project.security.model.Role;
import com.leverx.internship.project.user.repository.entity.User;
import com.leverx.internship.project.user.service.UserService;
import com.leverx.internship.project.user.web.converter.UserConverter;
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
class ProjectServiceImplTest {

  @Mock private ProjectRepository projectRepository;
  @Mock private UserService userService;
  private ProjectService projectServiceUnderTest;
  @Mock private ProjectConverter projectConverter;
  @Mock private UserConverter userConverter;
  @Mock private DepartmentRepository departmentRepository;
  @Mock private Clock clock;

  @BeforeEach
  public void init() {
    projectServiceUnderTest =
        new ProjectServiceImpl(projectRepository, projectConverter, userService, userConverter, departmentRepository);
  }

  @Test
  void findAllPositiveTest_ShouldReturnList() {
    ProjectResponse projectDto = new ProjectResponse();
    projectDto.setName("name");
    projectDto.setDescription("description");
    projectDto.setDateStart(clock.instant());
    projectDto.setDateEnd(clock.instant());
    List<ProjectResponse> expected = new ArrayList<>();
    expected.add(projectDto);
    Project project = new Project();
    project.setName("name");
    project.setDescription("description");
    project.setDateStart(clock.instant());
    project.setDateEnd(clock.instant());
    project.setCreatedAt(clock.instant());
    project.setUpdatedAt(clock.instant());
    Page<Project> projects = new PageImpl<>(List.of(project));
    when(projectRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(projects);

    ProjectResponse projectDto1 = new ProjectResponse();
    projectDto1.setName("name");
    projectDto1.setDescription("description");
    projectDto1.setDateStart(clock.instant());
    projectDto1.setDateEnd(clock.instant());
    when(projectConverter.toProjectResponse(any(Project.class))).thenReturn(projectDto1);

    List<ProjectResponse> actual = projectServiceUnderTest.findAll(0, 3, new ProjectParamRequest("name", null));

    assertEquals(expected.size(), actual.size());
  }

  @Test
  void saveProjectTest_ShouldCreate() {
    ProjectResponse expected = new ProjectResponse();
    expected.setName("name");
    expected.setDescription("description");
    expected.setDateStart(clock.instant());
    expected.setDateEnd(clock.instant());

    ProjectBodyRequest projectDto = new ProjectBodyRequest();
    projectDto.setName("name");
    projectDto.setDescription("description");
    projectDto.setDateStart(clock.instant());
    projectDto.setDateEnd(clock.instant());

    Project project = new Project();
    project.setName("name");
    project.setDescription("description");
    project.setDateStart(clock.instant());
    project.setDateEnd(clock.instant());
    project.setCreatedAt(clock.instant());
    project.setUpdatedAt(clock.instant());
    when(projectConverter.toEntity(projectDto)).thenReturn(project);

    ProjectResponse projectDto1 = new ProjectResponse();
    projectDto1.setName("name");
    projectDto1.setDescription("description");
    projectDto1.setDateStart(clock.instant());
    projectDto1.setDateEnd(clock.instant());
    when(projectConverter.toProjectResponse(any(Project.class))).thenReturn(projectDto1);

    Project project1 = new Project();
    project1.setName("name");
    project1.setDescription("description");
    project1.setDateStart(clock.instant());
    project1.setDateEnd(clock.instant());
    project1.setCreatedAt(clock.instant());
    project1.setUpdatedAt(clock.instant());
    when(projectRepository.save(any(Project.class))).thenReturn(project1);

    final ProjectResponse result = projectServiceUnderTest.create(projectDto);

    assertEquals(expected, result);
  }

  @Test
  void FindByIdTest_ShouldReturnProject() {

    final ProjectResponse expectedResult = new ProjectResponse();
    expectedResult.setId(1);
    expectedResult.setName("name");
    expectedResult.setDescription("description");
    expectedResult.setDateStart(clock.instant());
    expectedResult.setDateEnd(clock.instant());

    final Optional<Project> projectOptional = Optional.of(
        new Project(1, "name", "description", clock.instant(), clock.instant(),
            clock.instant(), clock.instant(), "createdBy", "updatedBy", Set.of(
            new User(1, "firstName", "lastName", "email", "password", false, Role.ADMIN,
                clock.instant(), clock.instant(), Set.of(),
                new Department(1, "name", "description", clock.instant(), "createdBy",
                    clock.instant(), "updatedBy", List.of(), Set.of())))));
    when(projectRepository.findById(1)).thenReturn(projectOptional);

    final UserResponse userResponse = new UserResponse(1, "email", "password", "firstName",
        "lastName", Role.ADMIN, false);
    when(userService.findUserByEmail("email")).thenReturn(userResponse);

    when(userService.getCurrentUser()).thenReturn(new JwtUser(1, "email", "alex", "asdas", "ASdAD", true, Role.ADMIN.getAuthorities()));

    final ProjectUsersResponse projectUsersResponse = new ProjectUsersResponse();
    projectUsersResponse.setId(1);
    projectUsersResponse.setName("name");
    projectUsersResponse.setUsers(
        Set.of(
            new UserResponse(1, "email", "password", "firstName", "lastName", Role.ADMIN, false)));
    when(projectConverter.toProjectUsersResponse(any(Project.class))).thenReturn(
        projectUsersResponse);

    final ProjectResponse projectResponse = new ProjectResponse();
    projectResponse.setId(1);
    projectResponse.setName("name");
    projectResponse.setDescription("description");
    projectResponse.setDateStart(clock.instant());
    projectResponse.setDateEnd(clock.instant());
    when(projectConverter.toProjectResponse(any(Project.class))).thenReturn(projectResponse);

    final ProjectResponse result = projectServiceUnderTest.findById(1);

    assertEquals(expectedResult, result);
  }

  @Test
  void updateProjectTest_ShouldUpdate() {
    ProjectResponse expected = new ProjectResponse();
    expected.setId(1);
    expected.setName("name");
    expected.setDescription("new description");
    expected.setDateStart(clock.instant());
    expected.setDateEnd(clock.instant());

    ProjectBodyRequest projectDtoToUpdate = new ProjectBodyRequest();
    projectDtoToUpdate.setDescription("new description");

    Project project = new Project();
    project.setId(1);
    project.setName("name");
    project.setDescription("description");
    project.setDateStart(clock.instant());
    project.setDateEnd(clock.instant());
    project.setCreatedAt(clock.instant());
    project.setUpdatedAt(clock.instant());

    Optional<Project> projectOptional = Optional.of(project);
    when(projectRepository.findById(1)).thenReturn(projectOptional);

    Project project1 = new Project();
    project1.setName("name");
    project1.setDescription("description");
    project1.setDateStart(clock.instant());
    project1.setDateEnd(clock.instant());
    project1.setCreatedAt(clock.instant());
    project1.setUpdatedAt(clock.instant());
    when(projectConverter.toEntity(any(ProjectResponse.class))).thenReturn(project1);

    ProjectResponse projectDto = new ProjectResponse();
    projectDto.setId(1);
    projectDto.setName("name");
    projectDto.setDescription("new description");
    projectDto.setDateStart(clock.instant());
    projectDto.setDateEnd(clock.instant());
    when(projectConverter.toUpdatedProjectResponse(eq(projectDtoToUpdate), any(Project.class)))
        .thenReturn(projectDto);
    when(projectConverter.toProjectResponse(any(Project.class))).thenReturn(projectDto);

    Project project2 = new Project();
    project2.setName("name");
    project2.setDescription("new description");
    project2.setDateStart(clock.instant());
    project2.setDateEnd(clock.instant());
    project2.setCreatedAt(clock.instant());
    project2.setUpdatedAt(clock.instant());
    when(projectRepository.save(any(Project.class))).thenReturn(project2);

    final ProjectResponse result = projectServiceUnderTest.update(1, projectDtoToUpdate);
    result.setId(1);
    assertEquals(expected, result);
  }

  @Test
  void deleteTest_ShouldDelete() {
    projectServiceUnderTest.delete(1);

    verify(projectRepository).deleteById(1);
  }

  @Test
  void testAddUserToProject_ShouldAddUser() {
    ProjectUsersResponse expected = new ProjectUsersResponse();
    expected.setId(1);
    expected.setName("name");
    expected.setUsers(
        Set.of(new UserResponse(1, "email@mail.ru", "password", "Ivan", "Ivanov",Role.ADMIN,   true)));

    User user = new User();
    user.setId(1);
    user.setFirstName("Ivan");
    user.setLastName("Ivanov");
    user.setEmail("email@mail.ru");
    user.setPassword("password");
    user.setActive(true);
    user.setRole(Role.ADMIN);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    user.setProjects(Set.of(new Project(1, "name", "description", clock.instant(), clock.instant() , clock.instant(), clock.instant(), "mail", "mail", Set.of(user))));

    Optional<Project> projectOptional =
        Optional.of(
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
                new HashSet<>()));
    projectOptional.get().getEmployees().add(user);
    when(projectRepository.findById(1)).thenReturn(projectOptional);

    ProjectUsersResponse projectUsersResponse = new ProjectUsersResponse();
    projectUsersResponse.setId(1);
    projectUsersResponse.setName("name");
    projectUsersResponse.setUsers(new HashSet<>());
    when(projectConverter.toProjectUsersResponse(any(Project.class))).thenReturn(projectUsersResponse);

    UserResponse userResponse = new UserResponse(1, "email@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);
    when(userService.findById(1)).thenReturn(userResponse);

    User user2 = new User();
    user2.setId(1);
    user2.setFirstName("Ivan");
    user2.setLastName("Ivanov");
    user2.setEmail("email@mail.ru");
    user2.setPassword("password");
    user2.setActive(true);
    user2.setRole(Role.ADMIN);
    user2.setCreatedAt(clock.instant());
    user2.setUpdatedAt(clock.instant());

    final Set<User> users = Set.of(user2);
    when(userConverter.usersResponseSetToUserSet(Set.of(new UserResponse(1, "email@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true)))).thenReturn(users);

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

    Project project =
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
            new HashSet<>());
    when(projectRepository.save(any(Project.class))).thenReturn(project);

    ProjectUsersResponse result = projectServiceUnderTest.addUserToProject(1, 1);

    assertEquals(expected, result);
    verify(projectRepository).save(any(Project.class));
  }

  @Test
  void testDeleteUserFromProject_ShouldDeleteUser() {
    User user = new User();
    user.setId(1);
    user.setFirstName("Ivan");
    user.setLastName("Ivanov");
    user.setEmail("email@mail.ru");
    user.setPassword("password");
    user.setActive(true);
    user.setRole(Role.ADMIN);
    user.setCreatedAt(clock.instant());
    user.setUpdatedAt(clock.instant());
    UserResponse userDto =
        new UserResponse(1, "email@mail.ru", "password", "Ivan", "Ivanov", Role.ADMIN, true);

    when(userService.findById(1)).thenReturn(userDto);

    User user1 = new User();
    user1.setId(1);
    user1.setFirstName("Ivan");
    user1.setLastName("Ivanov");
    user1.setEmail("email@mail.ru");
    user1.setPassword("password");
    user1.setActive(true);
    user1.setRole(Role.ADMIN);
    user1.setCreatedAt(clock.instant());
    user1.setUpdatedAt(clock.instant());
    Optional<Project> projectOptional =
        Optional.of(
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
                Set.of(user)));
    when(projectRepository.findById(1)).thenReturn(projectOptional);

    ProjectUsersResponse projectUsersResponse = new ProjectUsersResponse();
    projectUsersResponse.setId(1);
    projectUsersResponse.setName("name");
    projectUsersResponse.setUsers(new HashSet<>());
    projectUsersResponse.addUser(userDto);
    when(projectConverter.toProjectUsersResponse(any(Project.class))).thenReturn(projectUsersResponse);

    User user2 = new User();
    user2.setId(1);
    user2.setFirstName("Ivan");
    user2.setLastName("Ivanov");
    user2.setEmail("email@mail.ru");
    user2.setPassword("password");
    user2.setActive(true);
    user2.setRole(Role.ADMIN);
    user2.setCreatedAt(clock.instant());
    user2.setUpdatedAt(clock.instant());

    Project project =
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
            new HashSet<>());
    project.getEmployees().add(user2);
    when(projectRepository.save(any(Project.class))).thenReturn(project);

    projectServiceUnderTest.deleteUserFromProject(1,1);

    verify(projectRepository).save(any(Project.class));
  }

}