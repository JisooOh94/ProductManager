package com.musinsa.product.exception;

import com.musinsa.product.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(CustomException.class)
  @ResponseBody
  public ResponseEntity<ApiError> handleCustomException(CustomException ex) {
    ApiError errorResponse = new ApiError(ex.getHttpStatus(), ex.getMessage());
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ApiError> handleException(Exception ex) {
    ApiError errorResponse = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred at server. Please contact to service manager.");
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }
}
