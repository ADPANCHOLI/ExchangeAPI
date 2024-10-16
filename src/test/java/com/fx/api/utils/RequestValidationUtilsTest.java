package com.fx.api.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RequestValidationUtilsTest {

  @ParameterizedTest
  @CsvSource({"USD", "EUR", "INR"})
  void testValidateCurrencyCode_ValidCode(String validCurrencyCode) {
    assertTrue(RequestValidationUtils.validateCurrencyCode(validCurrencyCode));
  }

  @ParameterizedTest
  @CsvSource({"US", "USDE", "123", "usd"})
  void testValidateCurrencyCode_InvalidCode(String invalidCurrencyCode) {
    assertFalse(RequestValidationUtils.validateCurrencyCode(invalidCurrencyCode));
  }

  @ParameterizedTest
  @CsvSource({"USD,EUR,INR", "GBP,JPY,AUD"})
  void testValidateExpectedCurrencyCode_ValidCodes(String validCurrencyCode) {
    assertTrue(RequestValidationUtils.validateExpectedCurrencyCode(validCurrencyCode));
  }

  @Test
  void testValidateExpectedCurrencyCode_InvalidCodes() {
    assertFalse(RequestValidationUtils.validateExpectedCurrencyCode("USD,EU,INR"));
    assertFalse(RequestValidationUtils.validateExpectedCurrencyCode("GBP,JPY,123"));
    assertFalse(RequestValidationUtils.validateExpectedCurrencyCode("usd,eur,inr"));
    assertFalse(RequestValidationUtils.validateExpectedCurrencyCode("USD,,INR"));
  }
}
