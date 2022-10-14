package com.leverx.internship.project.project.web.converter;

import com.leverx.internship.project.project.repository.entity.Project;
import com.leverx.internship.project.project.web.dto.request.ProjectBodyRequest;
import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import com.leverx.internship.project.project.web.dto.response.ProjectUsersResponse;
import com.leverx.internship.project.user.web.converter.UserConverter;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectConverter {
  private final ModelMapper mapper;
  private final UserConverter userConverter;

  public Project toEntity(@NonNull ProjectResponse projectResponse) {
    return mapper.map(projectResponse, Project.class);
  }

  public Project toEntity(@NonNull ProjectBodyRequest projectBodyRequest) {
    return mapper.map(projectBodyRequest, Project.class);
  }

  public ProjectUsersResponse toProjectUsersResponse(@NonNull Project project) {
    mapper
        .typeMap(Project.class, ProjectUsersResponse.class)
        .setPostConverter(usersListToUsersRespListConverter());
    return mapper.map(project, ProjectUsersResponse.class);
  }

  public ProjectResponse toUpdatedProjectResponse(
      @NonNull ProjectBodyRequest projectBodyRequest, Project project) {
    ProjectResponse projectResponse = toProjectResponse(project);
    mapper.map(projectBodyRequest, projectResponse);
    return projectResponse;
  }

  public ProjectResponse toProjectResponse(@NonNull Project project) {
    return mapper.map(project, ProjectResponse.class);
  }

  public Set<ProjectResponse> projectSetToProjectRespSet(Set<Project> projects) {
    if (projects != null) {
      Set<ProjectResponse> projectResponseSet = new HashSet<>();
      projects.forEach(project -> projectResponseSet.add(toProjectResponse(project)));
      return projectResponseSet;
    } else {
      return new HashSet<>();
    }
  }

  public Set<Project> projectsRespListToProjectList(Set<ProjectResponse> projectResponseSet) {
    if (projectResponseSet != null) {
      Set<Project> projects = new HashSet<>();
      projectResponseSet.forEach(projectResponse -> projects.add(toEntity(projectResponse)));
      return projects;
    } else {
      return new HashSet<>();
    }
  }

  public Converter<Project, ProjectUsersResponse> usersListToUsersRespListConverter() {
    return mappingContext -> {
      Project source = mappingContext.getSource();
      ProjectUsersResponse destination = mappingContext.getDestination();
      destination.setUsers(userConverter.usersSetToUsersResponseSet(source.getEmployees()));
      return mappingContext.getDestination();
    };
  }
}