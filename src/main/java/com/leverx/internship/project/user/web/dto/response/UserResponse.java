package com.leverx.internship.project.user.web.dto.response;

import com.leverx.internship.project.security.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private int id;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private Role role;
  private boolean active;
}