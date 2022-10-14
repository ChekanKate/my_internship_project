package com.leverx.internship.project.department.service;

import com.leverx.internship.project.department.web.dto.request.DepartmentBodyRequest;
import com.leverx.internship.project.department.web.dto.request.DepartmentParamRequest;
import com.leverx.internship.project.department.web.dto.response.DepartmentProjectsResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentResponse;
import com.leverx.internship.project.department.web.dto.response.DepartmentUsersResponse;
import java.util.List;

public interface DepartmentService {
  List<DepartmentResponse> findAll(int page, int size, DepartmentParamRequest departmentsParamRequest);

  DepartmentResponse findById(Integer id);

  DepartmentResponse create(DepartmentBodyRequest departmentBodyRequest);

  DepartmentResponse update(Integer id, DepartmentBodyRequest departmentBodyToUpdateRequest);

  void delete(Integer id);

  DepartmentUsersResponse addEmployeeToDepartment(Integer idDepartment, Integer idUser);

  DepartmentProjectsResponse addProjectToDepartment(Integer idDepartment, Integer idProject);

  void removeProjectFromDepartment(Integer idDepartment, Integer idProject);

  DepartmentUsersResponse findAllUsersInDepartment(Integer idDep);

  DepartmentProjectsResponse findAllProjectsInDepartment(Integer idDep);
}
