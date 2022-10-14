package com.leverx.internship.project.project.service;

import com.leverx.internship.project.project.web.dto.request.ProjectBodyRequest;
import com.leverx.internship.project.project.web.dto.request.ProjectParamRequest;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.project.web.dto.response.ProjectUsersResponse;
import java.util.List;

public interface ProjectService {
  List<ProjectResponse> findAll(int page, int size, ProjectParamRequest params);

  ProjectResponse findById(Integer id);

  ProjectResponse create(ProjectBodyRequest projectBodyRequest);

  ProjectResponse update(Integer id, ProjectBodyRequest projectBodyRequestToUpdate);

  void delete(Integer id);

  ProjectUsersResponse findProjectUsers(Integer id);

  ProjectUsersResponse addUserToProject(Integer idProject, Integer idUser);

  void deleteUserFromProject(Integer idProject, Integer idUser);
}
