package com.codebyice.resilience.trade;

import com.codebyice.resilience.shared.ResilienceException;
import com.codebyice.resilience.shared.StockService;
import com.codebyice.resilience.shared.Trade;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class TradeService {

  private final StockService stockService;

  public TradeService(StockService stockService) {
    this.stockService = stockService;
  }

  String executeTrade(String ticker, int quantity) {
    log.info("executeTrade({}, {})", ticker, quantity);
    return stockService.executeTrade(ticker, quantity);
  }

}
