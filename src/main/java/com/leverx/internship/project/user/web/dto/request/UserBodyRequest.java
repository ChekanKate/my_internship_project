package com.leverx.internship.project.user.web.dto.request;

import com.leverx.internship.project.security.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyRequest {
  private int id;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private Role role;
  private boolean active;
}