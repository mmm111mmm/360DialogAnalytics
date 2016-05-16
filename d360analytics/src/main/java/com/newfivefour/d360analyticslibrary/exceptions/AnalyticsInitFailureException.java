package com.newfivefour.d360analyticslibrary.exceptions;

public class AnalyticsInitFailureException extends RuntimeException {
  public AnalyticsInitFailureException(String s) {
    super(s);
  }
  public AnalyticsInitFailureException(String s, Throwable t) {
    super(s, t);
  }

}
