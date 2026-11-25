package com.codebyice.resilience.bitly;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/bitly")
class BitlyController {

    private final BitlyService bitlyService;

    BitlyController(BitlyService bitlyService) {
        this.bitlyService = bitlyService;
    }

    @PostMapping()
    ResponseEntity<BitlyResponse> create(@RequestBody BitlyRequest request) {
        Integer shorten = bitlyService.shorten(request.longUrl());
        URI location = MvcUriComponentsBuilder
                .fromMethodCall(on(BitlyController.class).shorten(shorten))
                .build()
                .toUri();
        return ResponseEntity.created(location)
                .body(new BitlyResponse(location.toString()));
    }

    @GetMapping("/{id}")
    ResponseEntity<BitlyResponse> shorten(@PathVariable("id") Integer id) {
        String longUrl = bitlyService.get(id).orElseThrow();
        return ResponseEntity.status(HttpStatus.FOUND.value())
                .header(HttpHeaders.LOCATION, longUrl)
                .body(new BitlyResponse(longUrl));
    }
}
