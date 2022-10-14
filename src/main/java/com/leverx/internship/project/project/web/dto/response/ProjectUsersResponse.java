package com.leverx.internship.project.project.web.dto.response;

import com.leverx.internship.project.user.web.dto.response.UserResponse;
import java.util.Set;
import lombok.Data;

@Data
public class ProjectUsersResponse {
  private int id;
  private String name;
  private Set<UserResponse> users;

  public void addUser(UserResponse userResponse) {
    this.users.add(userResponse);
  }

  public void removeUser(UserResponse userResponse) {
    this.users.remove(userResponse);
  }
}
