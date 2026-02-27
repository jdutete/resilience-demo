package com.codebyice.resilience.shared;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class StockService {

  private final RestClient restClient;


  public StockService(RestClient restClient) {
    this.restClient = restClient;
  }

  // High-priority service
  @Bulkhead(name = "tradeService")
  public String executeTrade(String ticker, int quantity) {
    log.info("executeTrade({}, {})", ticker, quantity);
    return restClient.post()
        .uri("/api/external/trade")
        .body(new Trade(ticker, quantity))
        .retrieve()
        .body(String.class);
  }

  // Low-priority service that frequently slows down
  @Bulkhead(name = "newsService", fallbackMethod = "newsFallback")
  public List<String> getStockNews(String ticker) {
    log.info("getStockNews({})", ticker);
    return restClient.get()
        .uri("/api/external/news/{id}", ticker)
        .retrieve()
        .body(List.class);
  }

//  // FALLBACKS
//  public String tradeFallback(String ticker, int quantity, BulkheadFullException t) {
//    return "Trade system is currently at max capacity. Please try again in a few seconds.";
//  }

  public List<String> newsFallback(String ticker, BulkheadFullException t) {
    // Return cached news or a friendly message instead of an error
    return List.of("News service currently unavailable. Showing last-known headlines.");
  }
}