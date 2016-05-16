package com.newfivefour.d360analyticslibrary.exceptions;

public class DatabaseFailureException extends RuntimeException {
  public DatabaseFailureException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
