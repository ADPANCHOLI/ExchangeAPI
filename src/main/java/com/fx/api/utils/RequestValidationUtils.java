package com.fx.api.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestValidationUtils {

  private static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";

  private RequestValidationUtils() {}

  public static boolean validateCurrencyCode(final String currencyCode) {
    Pattern pattern = Pattern.compile(CURRENCY_CODE_REGEX);
    Matcher matcher = pattern.matcher(currencyCode);
    return matcher.matches();
  }

  public static boolean validateExpectedCurrencyCode(final String expectedCurrencyCode) {
    String[] strArray = expectedCurrencyCode.split(",");
    List<String> strList = Arrays.asList(strArray);
    return strList.stream().allMatch(RequestValidationUtils::validateCurrencyCode);
  }
}
