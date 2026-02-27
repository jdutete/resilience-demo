package com.codebyice.resilience.stock;

import org.springframework.boot.restclient.autoconfigure.RestClientBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  // Spring Boot 4 automatically provides a RestClient.Builder bean
//  @Bean
//  public RestClient stockRestClient(RestClient.Builder builder) {
//    return builder
////        .baseUrl("https://api.external-stocks.com")
//        .defaultHeader("Accept", "application/json")
//        .build();
//  }

  @Bean
  public RestClient stockRestClient(RestClient.Builder builder,
      RestClientBuilderConfigurer configurer) {
    // configurer.configure(builder) ensures metrics/logging are attached
    return configurer.configure(builder)
        .baseUrl("http://localhost:8080")
        .build();
  }
}
