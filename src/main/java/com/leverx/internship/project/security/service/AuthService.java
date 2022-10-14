package com.leverx.internship.project.security.service;

import com.leverx.internship.project.security.web.dto.request.AuthenticationRequestDto;
import com.leverx.internship.project.security.web.dto.response.AuthenticationResponseDto;

public interface AuthService {
  AuthenticationResponseDto authentication(AuthenticationRequestDto authenticationRequestDto);
}
