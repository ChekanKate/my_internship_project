package com.leverx.internship.project.project.web.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
  private int id;
  private String name;
  private String description;
  private Instant dateStart;
  private Instant dateEnd;
}
