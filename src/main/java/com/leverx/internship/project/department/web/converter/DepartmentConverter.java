package com.leverx.internship.project.department.web.converter;

import com.leverx.internship.project.department.repository.entity.Department;
import com.leverx.internship.project.department.web.dto.request.DepartmentBodyRequest;
import com.leverx.internship.project.department.web.dto.response.DepartmentProjectsResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentUsersResponse;
import com.leverx.internship.project.project.web.converter.ProjectConverter;
import com.leverx.internship.project.user.web.converter.UserConverter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DepartmentConverter {

  private final ModelMapper mapper;
  private final UserConverter userConverter;
  private final ProjectConverter projectConverter;

  public Department toEntity(@NonNull DepartmentResponse departmentResponse) {
    return mapper.map(departmentResponse, Department.class);
  }

  public Department toEntity(@NonNull DepartmentBodyRequest departmentBodyRequest) {
    return mapper.map(departmentBodyRequest, Department.class);
  }

  public DepartmentResponse toUpdatedDepartmentDto(
      @NonNull DepartmentBodyRequest departmentBodyRequestUpdate,
      Department department) {
    DepartmentResponse departmentResponse = toDepartmentResponse(department);
    mapper.map(departmentBodyRequestUpdate, departmentResponse);
    return departmentResponse;
  }

  public Department toUpdatedEntity(@NonNull DepartmentUsersResponse departmentBodyRequestUpdate,
      Department department) {
    mapper.typeMap(DepartmentUsersResponse.class, Department.class)
        .setPostConverter(usersDtoListToUsersListConverter());
    mapper.map(departmentBodyRequestUpdate, department);
    return department;
  }

  public Department toUpdatedEntity(@NonNull DepartmentProjectsResponse departmentProjectsResponse,
      Department department) {
    mapper.typeMap(DepartmentProjectsResponse.class, Department.class)
        .setPostConverter(projectsDtoSetToProjectsSetConverter());
    mapper.map(departmentProjectsResponse, department);
    return department;
  }

  public DepartmentResponse toDepartmentResponse(@NonNull Department department) {
    return mapper.map(department, DepartmentResponse.class);
  }

  public DepartmentUsersResponse toDepartmentsUsersResponse(@NonNull Department department) {
    mapper.typeMap(Department.class, DepartmentUsersResponse.class)
        .setPostConverter(usersListToUsersDtoListConverter());
    return mapper.map(department, DepartmentUsersResponse.class);
  }

  public DepartmentProjectsResponse toDepartmentProjectsResponse(@NonNull Department department) {
    mapper.typeMap(Department.class, DepartmentProjectsResponse.class)
        .setPostConverter(projectSetToProjectsDtoSetConverter());
    return mapper.map(department, DepartmentProjectsResponse.class);
  }

  public Converter<DepartmentUsersResponse, Department> usersDtoListToUsersListConverter() {
    return mappingContext -> {
      DepartmentUsersResponse source = mappingContext.getSource();
      Department destination = mappingContext.getDestination();
      destination.setEmployees(userConverter.usersRespListToUserList(source.getEmployeesDto()));
      return mappingContext.getDestination();
    };
  }

  public Converter<Department, DepartmentUsersResponse> usersListToUsersDtoListConverter() {
    return mappingContext -> {
      Department source = mappingContext.getSource();
      DepartmentUsersResponse destination = mappingContext.getDestination();
      destination.setEmployeesDto(userConverter.userListToUserResponseList(source.getEmployees()));
      return mappingContext.getDestination();
    };
  }

  private Converter<Department, DepartmentProjectsResponse> projectSetToProjectsDtoSetConverter() {
    return mappingContext -> {
      Department source = mappingContext.getSource();
      DepartmentProjectsResponse destination = mappingContext.getDestination();
      destination.setProjectsDto(projectConverter.projectSetToProjectRespSet(source.getProjects()));
      return mappingContext.getDestination();
    };
  }

  public Converter<DepartmentProjectsResponse, Department> projectsDtoSetToProjectsSetConverter() {
    return mappingContext -> {
      DepartmentProjectsResponse source = mappingContext.getSource();
      Department destination = mappingContext.getDestination();
      destination.setProjects(
          projectConverter.projectsRespListToProjectList(source.getProjectsDto()));
      return mappingContext.getDestination();
    };
  }
}