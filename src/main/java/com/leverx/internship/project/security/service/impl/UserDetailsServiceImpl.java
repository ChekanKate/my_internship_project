package com.leverx.internship.project.security.service.impl;

import com.leverx.internship.project.security.model.JwtUser;
import com.leverx.internship.project.user.repository.UserRepository;
import com.leverx.internship.project.user.repository.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    return JwtUser.createFromEntity(user);
  }
}
