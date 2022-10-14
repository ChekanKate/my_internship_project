package com.leverx.internship.project.user.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserParamRequest {
  private String firstName;
  private String email;
  private String lastName;
}
