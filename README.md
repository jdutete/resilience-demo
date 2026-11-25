# resilience-demo

A Spring Boot 4 demo of the Resilience4j Bulkhead pattern.

## How the bulkheads work

`RemoteController` simulates a slow external service. Each call to
`/api/external/trade` or `/api/external/news/{ticker}` sleeps for 1 second.
`StockService` calls these endpoints through a `RestClient`.

| Bulkhead       | Method          | Max concurrent calls | Max wait | When full                      |
|----------------|-----------------|----------------------|----------|--------------------------------|
| `tradeService` | `executeTrade`  | 5                    | `0ms`    | HTTP 429 (`GlobalResilienceHandler`) |
| `newsService`  | `getStockNews`  | 5                    | `100ms`  | `newsFallback` message (HTTP 200)    |

The configuration is in `src/main/resources/application.yaml`.

A single request never fills a bulkhead. To test a bulkhead, send more than
5 requests at the same time.

## Test the news bulkhead

1. Start the application.
2. Send 10 requests at the same time:

```bash
seq 1 10 | xargs -P 10 -I{} curl -s -w '\n' http://localhost:8080/api/news/TSLA
```

**Expected result:** About 5 responses come from the remote service. The other
responses show the fallback message:

```
["News from Remote TSLA 1"]
["News service currently unavailable. Showing last-known headlines."]
...
```

If you send 5 requests or fewer (`-P 5`), all responses come from the remote
service.

## Test the trade bulkhead

The `tradeService` bulkhead has no fallback. `GlobalResilienceHandler` returns
HTTP 429 when the bulkhead is full.

1. Start the application.
2. Send 10 requests at the same time:

```bash
seq 1 10 | xargs -P 10 -I{} curl -s -o /dev/null -w '%{http_code}\n' \
  -X POST http://localhost:8080/api/trades \
  -H 'Content-Type: application/json' -d '{"ticker":"TSLA","quantity":50}'
```

**Expected result:** You get 5 `201` codes and 5 `429` codes. The max wait is
`0ms`, so extra calls fail immediately.

## Load test with Artillery

`load-test/bulkhead-test.yaml` sends a baseline load and then a spike of trade
requests. The spike fills the `tradeService` bulkhead.

1. Install Artillery:

```bash
npm install -g artillery
```

2. Start the application.
3. Run the load test:

```bash
artillery run load-test/bulkhead-test.yaml
```

4. To see each HTTP response, run the load test in debug mode:

```bash
DEBUG=http:response artillery run load-test/bulkhead-test.yaml
```

**Expected result:** During the spike phase, the report shows `429` responses
for some trade requests.

## See the bulkhead state

1. Add `bulkheads` and `bulkheadevents` to
   `management.endpoints.web.exposure.include` in `application.yaml`.
2. Start the application.
3. Do one of the tests above.
4. Open these URLs:
   - `http://localhost:8080/actuator/bulkheads`
   - `http://localhost:8080/actuator/bulkheadevents/newsService`

The events list shows each `CALL_PERMITTED`, `CALL_REJECTED`, and
`CALL_FINISHED` event.
