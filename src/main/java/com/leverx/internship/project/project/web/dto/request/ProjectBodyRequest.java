package com.leverx.internship.project.project.web.dto.request;

import java.time.Instant;
import lombok.Data;

@Data
public class ProjectBodyRequest {
  private String name;
  private String description;
  private Instant dateStart;
  private Instant dateEnd;
}
