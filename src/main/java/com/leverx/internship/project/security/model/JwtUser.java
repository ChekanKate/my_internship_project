package com.leverx.internship.project.security.model;

import com.leverx.internship.project.user.repository.entity.User;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUser implements UserDetails {

  private final int id;
  private final String email;
  private final String firstName;
  private final String lastName;
  private final String password;
  private final boolean enabled;
  private final Set<SimpleGrantedAuthority> authorities;

  public JwtUser(
      int id,
      String email,
      String firstName,
      String lastName,
      String password,
      boolean enabled,
      Set<SimpleGrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.enabled = enabled;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return enabled;
  }

  @Override
  public boolean isAccountNonLocked() {
    return enabled;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return enabled;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getId() {
    return id;
  }

  public static JwtUser createFromEntity(User user) {
    return new JwtUser(
        user.getId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getPassword(),
        user.isActive(),
        user.getRole().getAuthorities());
  }
}