package com.leverx.internship.project.project.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectParamRequest {
  private String name;
  private String description;
}
