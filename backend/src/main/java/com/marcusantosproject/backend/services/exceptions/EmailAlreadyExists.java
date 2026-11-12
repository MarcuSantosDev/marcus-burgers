package com.marcusantosproject.backend.services.exceptions;

public class EmailAlreadyExists extends RuntimeException {
  public EmailAlreadyExists(String message) {
    super(message);
  }
}
