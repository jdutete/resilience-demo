package com.codebyice.resilience;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalResilienceHandler {

  /**
   * Catches BulkheadFullException and returns a standardized 429 error.
   * Uses the SB4 'ProblemDetail' for RFC-9457 compliant errors.
   */
  @ExceptionHandler(BulkheadFullException.class)
  public ProblemDetail handleBulkheadFull(BulkheadFullException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.TOO_MANY_REQUESTS,
        "The server is currently processing the maximum number of concurrent requests."
    );

    problemDetail.setTitle("Service Limit Reached");
    problemDetail.setProperty("timestamp", Instant.now());

    return problemDetail;
  }
}
