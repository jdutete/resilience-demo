package com.codebyice.resilience;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TraceResponseFilter extends OncePerRequestFilter {

  private final ObjectProvider<Tracer> tracerProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // Wrap the response to intercept headers before the body is committed
    HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response) {
      @Override
      public void setStatus(int sc) {
        appendTraceHeader();
        super.setStatus(sc);
      }

      @Override
      public void sendError(int sc) throws IOException {
        appendTraceHeader();
        super.sendError(sc);
      }

      private void appendTraceHeader() {
        // Safely fetch tracer from the provider
        tracerProvider.ifAvailable(tracer -> {
          if (tracer.currentSpan() != null && !response.isCommitted()) {
            response.setHeader("X-Trace-Id", tracer.currentSpan().context().traceId());
          }
        });
      }
    };

    // Standard logic for pre-commit setting
    tracerProvider.ifAvailable(tracer -> {
      if (tracer.currentSpan() != null) {
        response.setHeader("X-Trace-Id", tracer.currentSpan().context().traceId());
      }
    });

    filterChain.doFilter(request, wrappedResponse);
  }
}