package com.leverx.internship.project.security.model;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
public enum Role {
  ADMIN(Set.of(Permission.USER_DELETE, Permission.USER_WRITE, Permission.USER_READ)),
  EMPLOYEE(Set.of(Permission.USER_READ)),
  LEAD(Set.of(Permission.USER_WRITE, Permission.USER_READ));

  private final Set<Permission> permissions;

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public Set<SimpleGrantedAuthority> getAuthorities() {
    return getPermissions().stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toSet());
  }
}