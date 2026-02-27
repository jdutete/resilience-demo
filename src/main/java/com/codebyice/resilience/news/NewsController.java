package com.codebyice.resilience.news;

import com.codebyice.resilience.shared.StockService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

  private final StockService stockService;

  public NewsController(StockService stockService) {
    this.stockService = stockService;
  }

  @GetMapping(path = "/{ticker}", version = "1.0")
  public List<String> getStockNews(@PathVariable String ticker) {
    log.info("getStockNews({})", ticker);
    List<String> stockNews = stockService.getStockNews(ticker);
    log.info("getStockNews({})", stockNews);
    return stockNews;
  }
}
