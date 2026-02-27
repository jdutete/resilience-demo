package com.codebyice.resilience.remote;

import com.codebyice.resilience.shared.Trade;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/")
@Slf4j
public class RemoteController {

  private final RemoteService remoteService;

  public RemoteController(RemoteService remoteService) {
    this.remoteService = remoteService;
  }

  @PostMapping(value = "trade", version = "1.0")
  public String trade(@RequestBody Trade tradeRequest) {
    log.info("trade({})", tradeRequest);
    return remoteService.executeTrade(tradeRequest);
  }

  @GetMapping(value = "news/{ticker}", version = "1.0")
  public List<String> getNews(@PathVariable(name = "ticker") String ticker) {
    log.info("getNews({})", ticker);
    return remoteService.getStockNews(ticker);
  }

}
