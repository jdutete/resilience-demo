package com.codebyice.resilience.trade;

import com.codebyice.resilience.shared.StockService;
import com.codebyice.resilience.shared.Trade;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trades")
@Slf4j
public class TradeController {

  private final TradeService tradeService;

  public TradeController(TradeService tradeService) {
    this.tradeService = tradeService;
  }

  // SB4 Native Versioning: maps to 'X-API-VERSION: 1.0' or similar
  @PostMapping(version = "1.0")
  public ResponseEntity<String> executeTrade(@RequestBody Trade request) {
    log.info("executeTrade({})", request);
    String response = tradeService.executeTrade(request.ticker(), request.quantity());
    log.info("executeTrade({}, {})", request, response);
//    return ResponseEntity.created(response);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }


}
