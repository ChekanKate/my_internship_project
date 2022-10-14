package com.leverx.internship.project.auditor.impl;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuditorAwareImpl implements AuditorAware<String> {
  @NotNull
  @Override
  public Optional<String> getCurrentAuditor() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return Optional.of(((UserDetails) principal).getUsername());
  }
}
