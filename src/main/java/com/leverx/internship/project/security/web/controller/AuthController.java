package com.leverx.internship.project.security.web.controller;

import com.leverx.internship.project.security.service.AuthService;
import com.leverx.internship.project.security.web.dto.request.AuthenticationRequestDto;
import com.leverx.internship.project.security.web.dto.response.AuthenticationResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public AuthenticationResponseDto login(
      @RequestBody AuthenticationRequestDto authenticationRequestDto) {
    return authService.authentication(authenticationRequestDto);
  }
}
