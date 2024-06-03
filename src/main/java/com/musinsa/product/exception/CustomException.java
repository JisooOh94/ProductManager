package com.musinsa.product.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

  private final HttpStatus httpStatus;

  public CustomException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public CustomException(HttpStatus httpStatus, String message, Throwable throwable) {
    super(message, throwable);
    this.httpStatus = httpStatus;
  }
}
