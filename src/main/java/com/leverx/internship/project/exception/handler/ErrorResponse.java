package com.leverx.internship.project.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
  private String httpStatus;
  private String message;

  ErrorResponse(HttpStatus status, String message) {
    this.httpStatus = status.toString();
    this.message = message;
  }
}