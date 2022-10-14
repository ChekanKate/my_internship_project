package com.leverx.internship.project.department.web.dto.response;

import com.leverx.internship.project.project.web.dto.response.ProjectResponse;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentProjectsResponse {

  private int id;
  private String name;
  private Set<ProjectResponse> projectsDto;

  public void addProject(ProjectResponse project) {
    this.projectsDto.add(project);
  }

  public void removeProject(ProjectResponse project) {
    this.projectsDto.remove(project);
  }
}