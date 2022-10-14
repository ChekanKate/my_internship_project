package com.leverx.internship.project.department.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse implements Comparable<DepartmentResponse> {

  private int id;
  private String name;
  private String description;

  @Override
  public int compareTo(@NotNull DepartmentResponse departmentResponse) {
    return Integer.compare(id, departmentResponse.getId());
  }
}