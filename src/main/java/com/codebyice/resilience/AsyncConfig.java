package com.codebyice.resilience;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;

@Configuration
public class AsyncConfig {

  @Bean
  public ContextPropagatingTaskDecorator contextPropagatingTaskDecorator() {
    // This is the magic bean that fixes missing Trace IDs in async/virtual threads
    return new ContextPropagatingTaskDecorator();
  }
}
