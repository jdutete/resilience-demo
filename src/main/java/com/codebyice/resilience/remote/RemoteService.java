package com.codebyice.resilience.remote;

import com.codebyice.resilience.shared.Trade;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteService {

  private final AtomicLong counter = new AtomicLong();

  String executeTrade(Trade tradeRequest) {
    log.info("executeTrade({})", tradeRequest);
    try {
      Thread.sleep(1000);
      return "yes";
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  List<String> getStockNews(String ticker) {
    log.info("getStockNews({})", ticker);
    try {
      Thread.sleep(1000);
      return List.of("News from Remote %s %s".formatted(ticker, counter.incrementAndGet()));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
