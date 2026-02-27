package com.codebyice.resilience.shared;

import lombok.Getter;

@Getter
public class ResilienceException extends RuntimeException{

  private  static final long serialVersionUID = 1L;
  private int code;

  public  ResilienceException(String message, Throwable cause,  int code) {
    super(message, cause);
    this.code = code;
  }
}
