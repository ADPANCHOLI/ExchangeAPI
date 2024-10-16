package com.fx.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.api.exception.InvalidInputException;
import com.fx.api.exception.ResponseParsingException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FxService {

  private static final Logger logger = LoggerFactory.getLogger(FxService.class);

  @Value("${fx.host}")
  String host;

  @Value("${token}")
  String token;

  public Mono<String> getExchangeRates(
      final List<String> requiredCurrencyCodeList, final String baseCurrencyCode) {
    WebClient webClient = WebClient.builder().baseUrl(host).build();
    return webClient
        .get()
        .uri(
            String.format(
                "/latest?apikey=%s&currencies=%s&base_currency=%s",
                token, requiredCurrencyCodeList.get(0), baseCurrencyCode))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
        .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
        .bodyToMono(String.class);
  }

  public Double convertRates(
      final String requiredCurrencyCode, final String baseCurrencyCode, final double amount)
      throws ResponseParsingException {
    ObjectMapper objectMapper = new ObjectMapper();
    WebClient webClient = WebClient.builder().baseUrl(host).build();
    var response =
        webClient
            .get()
            .uri(
                String.format(
                    "/latest?apikey=%s&currencies=%s&base_currency=%s",
                    token, requiredCurrencyCode, baseCurrencyCode))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
            .bodyToMono(String.class);
    try {
      return objectMapper
              .readTree(response.block())
              .get("data")
              .get(requiredCurrencyCode)
              .get("value")
              .asDouble()
          * amount;
    } catch (JsonProcessingException e) {
      logger.error("Parsing failed for message {}", response.block());
      throw new ResponseParsingException(e.getMessage(), e);
    }
  }

  public Mono<Throwable> handleServerError(ClientResponse response) {
    return response
        .bodyToMono(String.class)
        .flatMap(
            body -> Mono.error(new RuntimeException("Server error: Try after sometime" + body)));
  }

  private Mono<? extends Throwable> handleClientError(ClientResponse response) {
    return response
        .bodyToMono(String.class)
        .flatMap(body -> Mono.error(new InvalidInputException("Server error: " + body)));
  }
}
