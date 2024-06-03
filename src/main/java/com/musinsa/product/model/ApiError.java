package com.musinsa.product.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {
  private final HttpStatus status;
  private final String message;

  public ApiError(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
