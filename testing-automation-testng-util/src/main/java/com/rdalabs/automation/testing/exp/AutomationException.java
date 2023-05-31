package com.rdalabs.automation.testing.exp;

public class AutomationException extends RuntimeException {

  private static final long serialVersionUID = 4833646631278348857L;

  public AutomationException() {
    super();
  }

  public AutomationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public AutomationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AutomationException(String message) {
    super(message);
  }

  public AutomationException(Throwable cause) {
    super(cause);
  }

}
