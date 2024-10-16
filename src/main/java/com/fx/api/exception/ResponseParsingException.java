package com.fx.api.exception;

public class ResponseParsingException extends Exception {

  public ResponseParsingException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
