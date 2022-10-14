package com.leverx.internship.project.department.service.impl;

import com.leverx.internship.project.department.repository.DepartmentRepository;
import com.leverx.internship.project.department.repository.entity.Department;
import com.leverx.internship.project.department.service.DepartmentService;
import com.leverx.internship.project.department.service.filter.DepartmentSpecification;
import com.leverx.internship.project.department.web.converter.DepartmentConverter;
import com.leverx.internship.project.department.web.dto.request.DepartmentBodyRequest;
import com.leverx.internship.project.department.web.dto.request.DepartmentParamRequest;
import com.leverx.internship.project.department.web.dto.response.DepartmentProjectsResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentUsersResponse;
import com.leverx.internship.project.exception.JwtAuthenticationException;
import com.leverx.internship.project.project.service.ProjectService;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.security.model.Role;
import com.leverx.internship.project.user.service.UserService;
import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentConverter departmentConverter;
  private final UserService userService;
  private final ProjectService projectService;

  @Override
  @Transactional(readOnly = true)
  public List<DepartmentResponse> findAll(int page, int size,
      DepartmentParamRequest departmentParamRequest) {
    Specification<Department> spec = Specification.where(
        DepartmentSpecification.departmentParamHasDescription(
            departmentParamRequest.getDescription()).and(
            DepartmentSpecification.departmentParamHasName(departmentParamRequest.getName())));
    Pageable paging = PageRequest.of(page, size);
    Page<Department> departmentPage = departmentRepository.findAll(spec, paging);
    List<DepartmentResponse> departmentsDto = new ArrayList<>();
    departmentPage.forEach(
        department -> departmentsDto.add(departmentConverter.toDepartmentResponse(department)));
    return departmentsDto;
  }

  @Override
  @Transactional(readOnly = true)
  public DepartmentResponse findById(Integer id) {
    Department department = getDepartment(id);
    UserResponse userResponse =
        userService.findUserByEmail(userService.getCurrentUser().getUsername());
    if (departmentConverter
        .toDepartmentsUsersResponse(department)
        .getEmployeesDto()
        .contains(userResponse)
        || Role.ADMIN.equals(userResponse.getRole())) {
      return departmentConverter.toDepartmentResponse(department);
    }
    throw new JwtAuthenticationException("You don't have permission for this action");
  }

  @Override
  @Transactional
  public DepartmentResponse create(DepartmentBodyRequest departmentBodyRequest) {
    Department department = departmentConverter.toEntity(departmentBodyRequest);
    return departmentConverter.toDepartmentResponse(departmentRepository.save(department));
  }

  @Override
  @Transactional
  public DepartmentResponse update(Integer id,
      DepartmentBodyRequest departmentBodyRequestToUpdate) {
    Department department = getDepartment(id);
    DepartmentResponse newDepartmentResponse = departmentConverter
        .toUpdatedDepartmentDto(departmentBodyRequestToUpdate, department);
    Department newDepartment = departmentConverter.toEntity(newDepartmentResponse);
    newDepartment.setId(id);
    newDepartment.setCreatedAt(department.getCreatedAt());
    newDepartment.setCreatedBy(department.getCreatedBy());
    return departmentConverter.toDepartmentResponse(departmentRepository.save(newDepartment));
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    getDepartment(id);
    departmentRepository.deleteById(id);
  }

  @Override
  @Transactional
  public DepartmentUsersResponse addEmployeeToDepartment(Integer idDepartment, Integer idUser) {
    DepartmentUsersResponse departmentUsersResponse = departmentConverter
        .toDepartmentsUsersResponse(getDepartment(idDepartment));
    UserResponse user = userService.findById(idUser);
    if (!departmentUsersResponse.getEmployeesDto().contains(user)) {
      departmentUsersResponse.addUser(user);
      Department department = departmentConverter
          .toUpdatedEntity(departmentUsersResponse, getDepartment(idDepartment));
      return departmentConverter.toDepartmentsUsersResponse(
          departmentRepository.save(department));
    } else {
      throw new DataIntegrityViolationException("User with id " + user.getId()
          + " already in this department");
    }
  }

  @Override
  @Transactional
  public DepartmentProjectsResponse addProjectToDepartment(Integer idDepartment,
      Integer idProject) {
    DepartmentProjectsResponse departmentProjectsResponse = departmentConverter
        .toDepartmentProjectsResponse(getDepartment(idDepartment));
    ProjectResponse project = projectService.findById(idProject);
    if (!departmentProjectsResponse.getProjectsDto().contains(project)) {
      departmentProjectsResponse.addProject(project);
      Department department = departmentConverter
          .toUpdatedEntity(departmentProjectsResponse, getDepartment(idDepartment));
      return departmentConverter.toDepartmentProjectsResponse(
          departmentRepository.save(department));
    } else {
      throw new DataIntegrityViolationException(
          "Project with id " + project.getId() + " already in this department");
    }
  }

  @Override
  @Transactional
  public void removeProjectFromDepartment(Integer idDepartment, Integer idProject) {
    DepartmentProjectsResponse departmentProjectsResponse = departmentConverter
        .toDepartmentProjectsResponse(getDepartment(idDepartment));
    ProjectResponse project = projectService.findById(idProject);
    if (departmentProjectsResponse.getProjectsDto().contains(project)) {
      departmentProjectsResponse.removeProject(project);
      Department department = departmentConverter
          .toUpdatedEntity(departmentProjectsResponse, getDepartment(idDepartment));
      departmentRepository.save(department);
    } else {
      throw new NotFoundException(
          "Project with id " + idProject + " doesn't  exists in department with id "
              + idDepartment);
    }
  }

  @Override
  @Transactional
  public DepartmentUsersResponse findAllUsersInDepartment(Integer idDep) {
    Department department = getDepartment(idDep);
    if (departmentHasUser(idDep)) {
      return departmentConverter.toDepartmentsUsersResponse(department);
    } else {
      throw new JwtAuthenticationException("You don't have permission for this action");
    }
  }

  @Override
  @Transactional
  public DepartmentProjectsResponse findAllProjectsInDepartment(Integer idDep) {
    Department department = getDepartment(idDep);
    if (departmentHasUser(idDep)) {
      return departmentConverter.toDepartmentProjectsResponse(department);
    } else {
      throw new JwtAuthenticationException("You don't have permission for this action");
    }
  }

  private boolean departmentHasUser(Integer idDep) {
    UserResponse currentUser = userService.findUserByEmail(
        userService.getCurrentUser().getUsername());
    Department department = departmentRepository.findDepartmentByEmployeesId(currentUser.getId())
        .orElseThrow(() -> new NotFoundException("You aren't a member of department"));
    return department.getId() == idDep || Role.ADMIN.equals(currentUser.getRole());
  }

  private Department getDepartment(Integer id) {
    return departmentRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Department with id: " + id + " doesn't exist"));
  }
}