package com.fx.api.configuration;

import com.fx.api.exception.InvalidInputException;
import com.fx.api.exception.ResponseParsingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<String> handleInvalidInputException(
      InvalidInputException ex, WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResponseParsingException.class)
  public ResponseEntity<String> handleParsingException(Exception ex, WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
