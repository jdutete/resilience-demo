package com.codebyice.resilience.bitly;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
class BitlyService {
    private final ConcurrentHashMap<Integer, String>  db = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public Integer shorten(String url) {
        log.info("Generating short url for {}", url);
        int id = counter.incrementAndGet();
        db.put(id, url);
        return id;
    }

    public Optional<String> get(int id) {
        log.info("Retrieving url for {}", id);
        return Optional.ofNullable(db.get(id));
    }
}
