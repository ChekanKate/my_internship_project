package com.leverx.internship.project.exception.handler;

import com.leverx.internship.project.exception.JwtAuthenticationException;
import com.leverx.internship.project.exception.ReportNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex) {
    ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND,
        String.format("Not found '%s'", ex.getMessage()));

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> constraintException(DataIntegrityViolationException ex) {
    ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST,
        String.format("Error because of constraint '%s'", ex.getMessage()));

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(JwtAuthenticationException.class)
  public ResponseEntity<ErrorResponse> jwtAuthException(JwtAuthenticationException ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN,
        String.format("Error of authentication '%s'", ex.getMessage()));
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<ErrorResponse> reportNotFoundException(ReportNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
        String.format("Search report error '%s'", ex.getMessage()));
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}