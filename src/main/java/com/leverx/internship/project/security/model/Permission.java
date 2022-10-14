package com.leverx.internship.project.security.model;

public enum Permission {
  USER_READ("user.read"),
  USER_WRITE("user.write"),
  USER_DELETE("user.delete");

  private final String permission;

  public String getPermission() {
    return permission;
  }

  Permission(String permission) {
    this.permission = permission;
  }

}
