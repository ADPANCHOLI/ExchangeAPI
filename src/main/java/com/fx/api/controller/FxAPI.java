package com.fx.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.api.exception.InvalidInputException;
import com.fx.api.exception.ResponseParsingException;
import com.fx.api.service.FxService;
import com.fx.api.utils.RequestValidationUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange-api")
public class FxAPI {

  private static final Logger logger = LoggerFactory.getLogger(FxAPI.class);

  private final FxService fxService;

  FxAPI(final FxService fxService) {
    this.fxService = fxService;
  }

  @GetMapping("/exchange-rate")
  public ResponseEntity<String> getExchangeRate(
      @RequestParam final String requiredCurrencyCodeList, @RequestParam final String currencyCode)
      throws InvalidInputException, ResponseParsingException {
    if (!RequestValidationUtils.validateExpectedCurrencyCode(requiredCurrencyCodeList)) {
      throw new InvalidInputException(
          String.format("Invalid Required Currency Code %s", requiredCurrencyCodeList));
    }
    if (!RequestValidationUtils.validateCurrencyCode(currencyCode)) {
      throw new InvalidInputException(String.format("Invalid Currency Code %s", currencyCode));
    }
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return ResponseEntity.ok(
          objectMapper
              .readTree(
                  fxService
                      .getExchangeRates(List.of(requiredCurrencyCodeList), currencyCode)
                      .block())
              .get("data")
              .toString());
    } catch (JsonProcessingException e) {
      logger.error("Parsing failed for message", e);
      throw new ResponseParsingException("Invalid Response received from backed", e);
    }
  }

  @GetMapping("/convert")
  public ResponseEntity<Double> convert(
      @RequestParam final String requiredCurrencyCode,
      @RequestParam final String currencyCode,
      @RequestParam final double amount)
      throws InvalidInputException, ResponseParsingException {
    if (!RequestValidationUtils.validateCurrencyCode(requiredCurrencyCode)) {
      logger.error("Invalid  Required Currency code {}", requiredCurrencyCode);
      throw new InvalidInputException(
          String.format("Invalid Required Currency Code %s", requiredCurrencyCode));
    }
    if (!RequestValidationUtils.validateCurrencyCode(currencyCode)) {
      logger.error("Invalid Currency code {}", currencyCode);
      throw new InvalidInputException(String.format("Invalid Currency Code %s", currencyCode));
    }
    return ResponseEntity.ok(fxService.convertRates(requiredCurrencyCode, currencyCode, amount));
  }
}
