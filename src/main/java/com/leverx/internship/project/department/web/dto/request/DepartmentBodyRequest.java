package com.leverx.internship.project.department.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentBodyRequest {
  private String name;
  private String description;
}
